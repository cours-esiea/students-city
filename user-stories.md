
## 2. User Stories (Student-Facing)

The following user stories focus on **student** interactions with the “Students City” app, covering everything from requesting an invite to exploring, reviewing, and organizing events.

### 2.1 Invite Request & Authentification

1. **Request an Invite via the App**
    
    - **Given** Chloé has just downloaded the “Students City” app on her phone,
    - **When** she opens the app, taps “Get an Invite,” and fills in her email and a short message,
    - **Then** the system should notify her that her request was received and is pending validation by an admin.
2. **Receive Invite Approval & Create Account**
    
    - **Given** Chloé’s invite request has been approved by the admin,
    - **When** she receives a confirmation email with instructions to finalize her account,
    - **Then** she can create a new account with her email and password, gaining access to the student features in the app.

1. **Log In with Existing Account**
    
    - **Given** Tom already had his invite request approved and created an account last week,
    - **When** he opens the app and enters his valid email and password,
    - **Then** the system should grant him access to the main screen where he can start exploring restaurants and other student spots.

1. **Edit Profile**
    
    - **Given** Chloé is already logged in,
    - **When** she navigates to her profile settings and updates her profile picture and display name,
    - **Then** her updated info should be visible on her profile page and in her interactions across the app.

---

### 2.2 Recherche & Navigation

5. **View Interactive Map**
    
    - **Given** Tom wants to see nearby restaurants,
    - **When** he opens the “Map” feature,
    - **Then** he should see a map of Dax with markers showing recommended eateries and other student-favorite spots.

1. **Filter by Category**
    
    - **Given** Chloé wants to find a list of dance clubs or bars with live music,
    - **When** she applies “Bars & Clubs” as her filter,
    - **Then** the map and/or list should only display those specific locations, sorted by distance or rating.

1. **Use Current Location**
    
    - **Given** Tom granted the app permission to use his location,
    - **When** he opens the “Nearby” tab,
    - **Then** the app should show restaurants ordered by proximity to his real-time position.

1. **View List with Distances**
    
    - **Given** Chloé wants a quick list rather than a map,
    - **When** she switches to the “List” view,
    - **Then** she should see establishments (bars, clubs, etc.) with distance details (e.g., 1.2 km away).

---

### 2.3 Contributions & Reviews

9. **Add a New Establishment (Pending Moderation)**
    
    - **Given** Tom discovers a hidden bistro that’s not listed,
    - **When** he taps “Add New Place,” fills in the bistro’s details, and submits,
    - **Then** the new entry should appear as “Pending Moderation” until the admin approves it.

1. **Write a Comment/Review**
    
    - **Given** Chloé just visited a new bar and wants to share her thoughts,
    - **When** she writes about the ambiance, music, and crowd and taps “Submit Review,”
    - **Then** her review should appear on the bar’s page, visible to other students (subject to moderation).

1. **Rate an Establishment**
    
    - **Given** Tom wants to rate a restaurant he tried last night,
    - **When** he selects a rating (e.g., 4 stars) and confirms,
    - **Then** his rating should be saved and factored into the overall score for that restaurant.

---

### 2.4 Other Potential Features (Student-Oriented)

12. **Receive Push Notifications**
    - **Given** Chloé has enabled push notifications,
    - **When** a new bar or club is added near her location or receives fresh reviews,
    - **Then** she should get a push notification prompting her to check out the details.

1. **Save as Favorite**
    
    - **Given** Tom wants to bookmark a particularly impressive Japanese restaurant,
    - **When** he taps the “Add to Favorites” button on the restaurant’s detail page,
    - **Then** the restaurant should appear in his personal “Favorites” list for quick access.

1. **Chat with Other Students**
    
    - **Given** Chloé wants to plan a meetup for a Friday night,
    - **When** she opens the “Chat” feature and starts a group conversation with other students in her contact list,
    - **Then** she can send invites, chat in real-time, and coordinate a meeting place.

1. **Organize an Event**
    
    - **Given** Tom wants to host a “Taste & Review” evening at a local café,
    - **When** he creates an event with a date, time, and location through the “Events” section,
    - **Then** invited students receive a notification with event details linked to the café’s page.