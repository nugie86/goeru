# Goeru
Introducing Goeru, the premier application that revolutionizes your learning experience. With a comprehensive and personalized learning system, our dedicated teachers are committed to unlocking your full potential. Experience the assurance of a guaranteed security system as we connect you with the best educators, ensuring a tranquil environment for focused study. At Goeru, we're not just an app; we're your trusted partners in education, paving the way for seamless and successful learning journeys.

![Project Banner](https://github.com/nugie86/goeru/blob/main/resources/logo%20goeru.png)

## Made by CH2PS561
- (ML) M004BSY0292 – Gilang Fajrul Falah – Sepuluh Nopember Institute of Technology - [Active]
- (ML) M004BSY1173 – Mugni Ahmad Hikam – Sepuluh Nopember Institute of Technology  - [Active]
- (ML)  M004BSY1920 – Ghaly Fahrian Ilyas – Sepuluh Nopember Institute of Technology  - [Active]
- (CC)  C318BSY3640 – Adrian Nugi Saputra – Universitas Sultan Ageng Tirtayasa - [Active]
- (CC) C008BSY4136 – Adiyatma Hilmy Kusuma Wijaya – Universitas Gadjah Mada - [Active]
- (MD) A548BKY4513 – Tegas Setiawan – Universitas Islam Negeri Yogyakarta - [Active]

## How to use Machine Learning Program
1. Install all the requirement needs, we recommend you to use VSCode as IDE
2. Create virtual environment using python 3.9.18
   - Tensorflow 2.10.0
   - Numpy 1.26.2
   - Scikit-learn 1.3.0
   - Tabulate 0.9.0
   - Pandas 2.1.1
3. Clone all the repositories
4. Run in your VSCode IDE

## How the Mobile Development works in this application
   - In Android app development, the use of ViewModel and Repository is part of the recommended architecture, commonly known as the MVVM (Model-View-ViewModel) pattern. 
     This pattern helps in separating concerns, making the codebase more modular, testable, and maintainable.
   - The ApiService interface is responsible for defining the endpoints and operations related to API.
   - The ViewModel is responsible for managing UI-related data. It holds and processes data needed by the UI and survives configuration changes (like screen rotation). 
     It interacts with the Repository to fetch or save data.
   - The activities called the functions from the view models, so the data will survive from any configuration changes (like device rotation) without the need to reload the data.

   - In conclusion, ApiService defines the API endpoints. Repository uses ApiService to fetch data and provides additional data handling logic if needed. ViewModel interacts with the Repository to fetch data. It
     exposes the fetched data to the UI through a LiveData or StateFlow.
