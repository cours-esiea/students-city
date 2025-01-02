# Students City

Students City is an exclusive, invite-only Android application designed for students in Dax, France. The app helps students discover and share the best local spots, from restaurants and bars to sports facilities, creating a trusted community-driven platform.

## Overview

- **Platform**: Android (Java)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Target Location**: Dax, France
- **Access**: Invite-only system
- **Maps Integration**: OpenStreetMap via OSMDroid

## Key Features

### 🔐 Exclusive Access

- Invite-only registration system
- Secure authentication
- Student-verified community

### 🗺️ Location Discovery

- Interactive map showing recommended places
- Distance-based listing of establishments
- Advanced filtering options (type, price range, hours)
- Real-time location-based recommendations

### 💬 Community Engagement

- Place reviews and ratings
- Detailed establishment information
- User-generated content (pending moderation)
- Profile customization

### 🌟 Additional Features

- Favorites system for quick access
- Push notifications for new places and reviews
- In-app chat for student communication
- Event organization tools

## Project Structure

```
com.example.studentscity/
├── model/
│   └── (entities, DTOs)
├── repository/
│   └── (data source handlers)
├── view/
│   ├── activity/
│   ├── fragment/
│   └── (UI components)
├── viewmodel/
│   └── (ViewModels)
└── utils/
    └── (helpers, constants)
```

## Technical Stack

- **Language**: Java
- **Architecture Pattern**: MVVM
- **Async Operations**: CompletableFuture
- **Maps**: OpenStreetMap (OSMDroid)
- **Database**: SQL-based
- **Network**: REST API

## Documentation

- [Project Guidelines](project-guidelines.md) - Detailed technical guidelines and architecture
- [Functional Specifications](spec-fonctionnelles.md) - Complete feature specifications
- [User Stories](user-stories.md) - Detailed user interactions and flows
- [Personas](persona.md) - Target user profiles

## Target Users

### Chloé (21, Communication Student)

- Nightlife enthusiast
- Social event organizer
- Looking for trending spots

### Tom (19, Culinary Arts Student)

- Local food explorer
- Budget-conscious diner
- Passionate about discovering new cuisines

## License

[[LICENSE]] project license (MIT)

## Contact

[Contact information to be added]
