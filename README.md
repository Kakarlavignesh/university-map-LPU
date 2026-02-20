# University Route Optimization System

An advanced campus mapping and navigation system built with Spring Boot and React.

## 🏗️ System Architecture

```mermaid
graph TD
    subgraph Frontend (React + Leaflet)
        UI[Map UI] --> Controller[Route Controller]
        Controller --> API[Axios API Client]
    end

    subgraph Backend (Spring Boot)
        API_END[REST Endpoints] --> MapService[Map Service]
        MapService --> Dijkstra[Dijkstra Algo]
        MapService --> Astar[A* Algo]
        MapService --> Repo[JPA Repositories]
    end

    subgraph Database (MySQL)
        Repo --> DB[(MySQL DB)]
    end
```

## 🚀 Deployment Guide

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL Server

### 1. Database Setup
1. Create a database: `CREATE DATABASE university_map;`
2. Run `docs/schema.sql` to create tables.
3. (Optional) Run `docs/data.sql` for sample data.

### 2. Backend Setup
1. Navigate to `backend/`
2. Update `src/main/resources/application.properties` with your MySQL credentials.
3. Run: `./mvnw spring-boot:run` (or use your IDE).

### 3. Frontend Setup
1. Navigate to `frontend/`
2. Install dependencies: `npm install`
3. Start development server: `npm run dev`
4. Access at `http://localhost:5173`

## 🛠️ API Documentation

### Get All Buildings
- **URL**: `/api/map/buildings`
- **Method**: `GET`
- **Response**: List of Building objects.

### Find Route
- **URL**: `/api/map/route`
- **Method**: `GET`
- **Params**:
  - `startId`: ID of source building.
  - `endId`: ID of destination building.
  - `algorithm`: `dijkstra` or `astar`.
  - `criteria`: `fastest`, `wheelchair`, or `nightsafe`.

## 🧠 Algorithms Used
- **Dijkstra**: Guaranteed shortest path based on edge weights.
- **A***: Faster heuristic-based search using Haversine distance as a heuristic.

## 🛡️ Special Features
- **Dynamic Rerouting**: Real-time handling of blocked paths.
- **Criteria Filters**: Wheelchair accessibility and night-safety path filtering.
- **Interactive UI**: Click-to-set start/end points on a high-end dark map.
