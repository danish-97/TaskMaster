**TaskMaster: A Task Management Application**

**Purpose:**
TaskMaster is a task management application designed to help users organize their tasks, deadlines, and priorities effectively. It aims to streamline task management and increase productivity by providing users with a centralized platform to create, track, and manage tasks. Targeting individuals who seek better task organization, TaskMaster offers a user-friendly interface to input tasks, set due dates, and prioritize activities. By visualizing tasks and deadlines, users can make informed decisions, allocate time efficiently, and achieve their goals with improved time management.

**Development Process:**
1. Initial Setup and Navigation: Chose Jetpack Compose for modern UI development and established a navigation system using a NavHost. Created the main screen layout, integrated an app drawer for navigation, and incorporated a top bar for consistent layout design.
2. Task Creation: Implemented functionality to create tasks with name, description, due date, and priority. Integrated user-friendly components like TextFields for input and buttons for actions like editing and deleting tasks.
3. Persistence with Room Database: Integrated Room database to store and retrieve task data, allowing users to access their tasks across app sessions and device reboots.
4. Task display:  Displayed tasks in a list using Jetpack Compose's LazyColumn, providing a dynamic and responsive interface. Implemented long press functionality on each task card which will show the edit and delete task options.
5. State Management with ViewModel: Utilized ViewModel architecture for managing UI states, data persistence, and business logic. Enabled data sharing between different screens while ensuring data consistency.
6. Task Details and Editing: Developed a task details screen to view and edit task details. Added a share button which will allow to share the task.
7. Calendar Integration: Added a calendar view to visualize tasks based on due dates, enhancing user awareness of upcoming deadlines. Tasks are color-coded based on priority, providing a quick overview.
8. Alert Dialog for Task Details: Created an alert dialog to display task details when clicking on a calendar date. Utilized Jetpack Compose's AlertDialog for a visually appealing and functional display.
9. Toast messages and Validation: Added toast messages to creating, editing and deleting the tasks. Also added validation to the text fields for task creation and editing.

**Grade-Bearing Requirements:**
- Multi-Screen Setup: Designed main screen, task creation screen, task editing screen and calendar view to fulfill the multi-screen requirement.
- Invoking: Implemented a Share button which can share a task to different apps.
- Floating Action Button: Added a floating action button for easy task creation, enhancing user interaction.
- Dialogs and Toasts: Utilized AlertDialog to display task details and implemented toast messages for user notifications.
- String Resource Usage: Employed string resources for all static text, facilitating localization and internationalization.
- Supports the languages @Danish and Maori
- Use LazyColumn to display the list of tasks in the home page
- Use OutlinedTextFields, Buttons, ExposedDropdowns, DropdownItems, Cards, and more for various user interactions.
- Responsive UI Design: Ensured all UI components adapt to both portrait and landscape orientations, providing a consistent experience.
- LiveData Integration: Utilized LiveData or StateFlow for managing UI states and real-time updates, enhancing data consistency.
- Animation and User Interaction: Enhanced user experience by adding animations for transitions and interactions, making the app engaging.
- Room Database Usage: Integrated Room database for efficient task data storage, retrieval, and management.
- Navigation and Intents: Implemented navigation between screens and enabled users to share their tasks.
- Top App Bar and Drawer: Designed a top app bar and integrated an app drawer for seamless menu access and navigation .

TaskMaster aims to simplify task management for users by providing a feature-rich application that covers task creation, organization, and visualization. The app's development journey encompassed setting up navigation, UI design, data storage, real-time updates, and user interaction. With TaskMaster, users can tackle their to-do lists effectively, meet deadlines, and achieve their objectives with improved task management.
