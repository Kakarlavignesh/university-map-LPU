import { useState, useEffect } from 'react'
import { MapContainer, TileLayer, Marker, Popup, Polyline, Tooltip, useMap, CircleMarker } from 'react-leaflet'
import axios from 'axios'
import { Navigation, Shield, Accessibility, Map as MapIcon, Settings, Info, Loader2 } from 'lucide-react'
import { motion, AnimatePresence } from 'framer-motion'
import 'leaflet/dist/leaflet.css'
import './App.css'

// Fix for default marker icons in Leaflet + Vite
import L from 'leaflet'
import icon from 'leaflet/dist/images/marker-icon.png'
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: iconRetina,
    iconUrl: icon,
    shadowUrl: iconShadow,
});

// We will use standard Leaflet dynamic styling instead of external images
// to ensure 100% visibility and zero "blank page" issues.

// Use your local IP so other devices on the network can connect to the backend
const API_BASE = 'http://172.26.83.216:8081/api/map'

function MapEffect({ center }) {
    const map = useMap()
    useEffect(() => {
        if (center) map.flyTo(center, 17)
    }, [center])
    return null
}

function App() {
    const [buildings, setBuildings] = useState([])
    const [startNode, setStartNode] = useState(null)
    const [endNode, setEndNode] = useState(null)
    const [route, setRoute] = useState([])
    const [algorithm, setAlgorithm] = useState('dijkstra')
    const [criteria, setCriteria] = useState('fastest')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(null)

    useEffect(() => {
        fetchBuildings()
    }, [])

    const fetchBuildings = async () => {
        try {
            const res = await axios.get(`${API_BASE}/buildings`)
            setBuildings(res.data)
        } catch (err) {
            console.error("Failed to fetch buildings", err)
            setError("Server connection failed. Make sure backend is running.")
        }
    }

    const findRoute = async () => {
        if (!startNode || !endNode) return
        setLoading(true)
        setError(null)
        try {
            const res = await axios.get(`${API_BASE}/route`, {
                params: {
                    startId: startNode.id,
                    endId: endNode.id,
                    algorithm,
                    criteria
                }
            })
            setRoute(res.data)
        } catch (err) {
            console.error("Failed to find route", err)
            setError("Path not found. Ensure locations are connected.")
        } finally {
            setLoading(false)
        }
    }

    const reset = () => {
        setStartNode(null)
        setEndNode(null)
        setRoute([])
    }

    const categorizedBuildings = buildings.reduce((acc, b) => {
        const type = b.type || 'OTHERS';
        if (!acc[type]) acc[type] = [];
        acc[type].push(b);
        return acc;
    }, {});

    return (
        <div className="app-container">
            <header className="app-header">
                <h1>UniMap Pro v2 - Campus Navigator</h1>
                <div className="status-badge">Live System</div>
            </header>

            {error && (
                <div className="fatal-error-header">
                    <Info size={16} />
                    <span>{error}</span>
                </div>
            )}

            {buildings.length === 0 && !error && (
                <div className="loading-overlay">
                    <Loader2 size={48} className="spin" />
                    <p>Fetching Campus Data...</p>
                </div>
            )}

            <div className="sidebar">
                <div className="logo">
                    <MapIcon size={32} />
                    <span>UniMap Pro v2</span>
                </div>

                <div className="search-section">
                    <div className="input-group">
                        <label>Starting Point</label>
                        <select
                            value={startNode?.id || ''}
                            onChange={(e) => setStartNode(buildings.find(b => b.id == e.target.value))}
                        >
                            <option value="">Select Building</option>
                            {Object.entries(categorizedBuildings).map(([type, list]) => (
                                <optgroup key={type} label={type}>
                                    {list.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                                </optgroup>
                            ))}
                        </select>
                    </div>

                    <div className="input-group">
                        <label>Destination</label>
                        <select
                            value={endNode?.id || ''}
                            onChange={(e) => setEndNode(buildings.find(b => b.id == e.target.value))}
                        >
                            <option value="">Select Building</option>
                            {Object.entries(categorizedBuildings).map(([type, list]) => (
                                <optgroup key={type} label={type}>
                                    {list.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                                </optgroup>
                            ))}
                        </select>
                    </div>

                    <div className="divider">Algorithm</div>

                    <div className="options-grid">
                        <div className={`option-card ${algorithm === 'dijkstra' ? 'active' : ''}`} onClick={() => setAlgorithm('dijkstra')}>
                            <Settings size={18} />
                            <span>Dijkstra</span>
                        </div>
                        <div className={`option-card ${algorithm === 'astar' ? 'active' : ''}`} onClick={() => setAlgorithm('astar')}>
                            <Navigation size={18} />
                            <span>A*</span>
                        </div>
                    </div>

                    <div className="divider">Route Mode</div>

                    <div className="options-grid grid-3">
                        <div className={`option-card ${criteria === 'fastest' ? 'active' : ''}`} onClick={() => setCriteria('fastest')}>
                            <Loader2 size={18} />
                            <span>Fast</span>
                        </div>
                        <div className={`option-card ${criteria === 'safest' ? 'active' : ''}`} onClick={() => setCriteria('safest')}>
                            <Shield size={18} />
                            <span>Safe</span>
                        </div>
                        <div className={`option-card ${criteria === 'nightsafe' ? 'active' : ''}`} onClick={() => setCriteria('nightsafe')}>
                            <Shield size={18} />
                            <span>Night</span>
                        </div>
                        <div className={`option-card ${criteria === 'wheelchair' ? 'active' : ''}`} onClick={() => setCriteria('wheelchair')}>
                            <Accessibility size={18} />
                            <span>Wheelchair</span>
                        </div>
                    </div>

                    <button
                        className="route-btn"
                        disabled={!startNode || !endNode || loading}
                        onClick={findRoute}
                    >
                        {loading ? <Loader2 className="spin" /> : 'Optimize Route'}
                    </button>

                    <button className="reset-btn" onClick={reset}>Clear Map</button>
                </div>

                {route.length > 0 && (
                    <div className="route-info">
                        <h3>Route Summary</h3>
                        <p>Steps: {route.length} nodes</p>
                        <div className="path-list">
                            {route.map((node, i) => (
                                <div key={i} className="path-item">
                                    <div className="dot" />
                                    <span>{node.name}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {error && <div className="error-msg">{error}</div>}
            </div>

            {/* Map Area */}
            <div className="map-wrapper">
                <MapContainer
                    center={[23.8103, 90.4125]} // Default center (adjust to your campus)
                    zoom={15}
                    style={{ height: '100%', width: '100%' }}
                >
                    <TileLayer
                        url="https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png"
                        attribution='&copy; OpenStreetMap contributors'
                    />

                    {/* Highlight Cluster 25-38 */}
                    {Array.isArray(buildings) && buildings.some(b => b?.name?.includes("Block 25")) && (
                        <Polyline
                            positions={[
                                [23.820, 90.420], [23.822, 90.420], [23.822, 90.422], [23.820, 90.422], [23.820, 90.420]
                            ]}
                            color="#f59e0b"
                            weight={2}
                            fill={true}
                            fillColor="#f59e0b"
                            fillOpacity={0.1}
                        >
                            <Popup>Academic Cluster (Blocks 25-38)</Popup>
                        </Polyline>
                    )}

                    {buildings.map(b => {
                        const isStart = startNode?.id === b.id;
                        const isEnd = endNode?.id === b.id;

                        const color = b.type === 'HOSTEL' ? '#10b981' :
                            b.type === 'ACADEMIC' ? '#3b82f6' :
                                b.type === 'SERVICE' ? '#ef4444' : '#6b7280';

                        // For Start/End nodes, we use a distinct pulsing circle for maximum visibility
                        if (isStart || isEnd) {
                            return (
                                <CircleMarker
                                    key={`circle-${b.id}`}
                                    center={[b.latitude, b.longitude]}
                                    radius={isEnd ? 12 : 10}
                                    fillColor={isStart ? '#10b981' : '#ef4444'}
                                    color="white"
                                    weight={3}
                                    opacity={1}
                                    fillOpacity={0.8}
                                    eventHandlers={{
                                        click: () => {
                                            if (!startNode) setStartNode(b)
                                            else if (!endNode) setEndNode(b)
                                        }
                                    }}
                                >
                                    <Tooltip direction="top" offset={[0, -10]} permanent>
                                        <span className={`tooltip-label ${isStart ? 'start-label' : ''} ${isEnd ? 'end-label' : ''}`}>
                                            {isStart ? "(START) " : "(DEST) "}{b.name}
                                        </span>
                                    </Tooltip>
                                    <Popup className="custom-popup">
                                        <div className="popup-header" style={{ borderBottomColor: color }}>
                                            <span className="type-badge" style={{ backgroundColor: color }}>{b.type}</span>
                                            <strong>{b.name}</strong>
                                        </div>
                                        <p>{b.description}</p>
                                        <div className="popup-actions">
                                            <button className="start-btn" onClick={() => setStartNode(b)}>Set as Start</button>
                                            <button className="end-btn" onClick={() => setEndNode(b)}>Set as Destination</button>
                                        </div>
                                    </Popup>
                                </CircleMarker>
                            )
                        }

                        return (
                            <Marker
                                key={b.id}
                                position={[b.latitude, b.longitude]}
                                eventHandlers={{
                                    click: () => {
                                        if (!startNode) setStartNode(b)
                                        else if (!endNode) setEndNode(b)
                                    }
                                }}
                            >
                                <Tooltip direction="top" offset={[0, -32]} opacity={1}>
                                    {b.name}
                                </Tooltip>
                                <Popup className="custom-popup">
                                    <div className="popup-header" style={{ borderBottomColor: color }}>
                                        <span className="type-badge" style={{ backgroundColor: color }}>{b.type}</span>
                                        <strong>{b.name}</strong>
                                    </div>
                                    <p>{b.description}</p>
                                    <div className="popup-actions">
                                        <button className="start-btn" onClick={() => setStartNode(b)}>Set as Start</button>
                                        <button className="end-btn" onClick={() => setEndNode(b)}>Set as Destination</button>
                                    </div>
                                </Popup>
                            </Marker>
                        )
                    })}

                    {route.length > 0 && (
                        <>
                            {/* Glowing effect shadow polyline */}
                            <Polyline
                                positions={route.map(n => [n.latitude, n.longitude])}
                                color="#3b82f6"
                                weight={10}
                                opacity={0.3}
                            />
                            {/* Main core polyline */}
                            <Polyline
                                positions={route.map(n => [n.latitude, n.longitude])}
                                color="#60a5fa"
                                weight={5}
                                opacity={1}
                            />
                        </>
                    )}

                    {startNode && <MapEffect center={[startNode.latitude, startNode.longitude]} />}
                </MapContainer>
            </div>
        </div>
    )
}

export default App
