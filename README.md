Original App Design Project
===

# Safe Out

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Safe Out is a security focused app, where you will be able to share your live location with your trusted ontacts. You can also set a status, depending on how your'e feeling (safe, alert/nervous, in danger), with every status change, your contacts will be notified and can react in time if you're not feeling safe at any given moment

### App Evaluation
- **Category:** Social/Navigation
- **Mobile:** Location, Maps and real-time - this three are used for seeing all of your contacts locations in real-time within the app
- **Story:** My peers and friends liked this app idea because everyone has been through a stressful situation and this app could help them in these situations.
As for the average user, especially in Mexico, people could really use an app like this fro staying in contact with their loved ones and it'll help them feel safer.
- **Market:** My app market is pretty big, as it's focused on young adults and teenagers (17 - 25). This is because when going out, people are not thinking about the risks. So having an ap that is simple to use, even when drunk or scared, will help them reach their homes safely by keeping this connection with other people.
Even though the marketing target are people between 17 and 25, I think people of all ages could use an app like this one when going out, so this app could really grow
- **Habit:** During the pandemic, people might not use the app as much, as it is for being safe when going out. But as we get out of this global crisis, an average user would use this app at least once a week (asuming that they go out once a week).
- **Scope:** Since we haven't seen any maps or live location functions for an android app,at this point I think this app would be pretty challenging. But since it is also a very simple concept, I think when I'm past the location and maps configuration, everything else will be easier.

## Product Spec

### 1. User Stories (Required and Optional)
 Trello with detailed activities: https://trello.com/b/Mq5Hbz6z/safeout-android-app

**Required Must-have Stories**

* Share your location in real-time
* See Other people's location 
* Set safety status
* Add people to your contacts list

**Optional Nice-to-have Stories**

* Connect with messenger for keeping in touch
* Create contact gropus for sharing location with a certain group of people
* Add people from QR or a different way than Facebooks friend list
* Notify contacts when safety status changes

### 2. Screen Archetypes

* Login
* Profile
* Detail
   * Location Screen
   * Set Status Creen
* Settings

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Status
* Map
* User

**Flow Navigation** (Screen to Screen)

* Log In
   * Map
* Map
   * Status
   * User
* Status
   * Map
   * User
   * [From Optional User Stories] Open Messenger App
* User
   * Map
   * Status
   * [From Optional User Stories] Settings
   * Log In

## Wireframes
<img src="https://github.com/SebastianJuncosL/SafeOutApp/blob/main/Wireframe%20image.png" width=600>

## Schema 
<img src="https://github.com/SebastianJuncosL/SafeOutApp/blob/main/SafeOut%20DBMS%20Schema.png" width=600>

### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
