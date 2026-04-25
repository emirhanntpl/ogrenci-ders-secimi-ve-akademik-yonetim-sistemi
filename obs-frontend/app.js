// HTML'deki elemanları seçiyoruz
const loginForm = document.getElementById('login-form');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');
const errorMessage = document.getElementById('error-message');

// Backend API'mizin ana adresi
const API_URL = 'http://localhost:8080';

// Form gönderildiğinde çalışacak fonksiyon
loginForm.addEventListener('submit', async (event) => {
    // Sayfanın yeniden yüklenmesini engelliyoruz (böylece JS ile kendimiz istek atabiliriz)
    event.preventDefault(); 

    const username = usernameInput.value;
    const password = passwordInput.value;

    try {
        // Backend'deki /auth/login adresine POST isteği (fetch) atıyoruz
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            // Giriş başarılıysa arka taraftan JWT Token'ı alıyoruz
            const data = await response.json();
            
            // Token'ı tarayıcı hafızasına (localStorage) kaydediyoruz
            localStorage.setItem('jwtToken', data.token);
            
            errorMessage.style.color = 'green';
            errorMessage.textContent = 'Giriş başarılı! Ana sayfaya yönlendiriliyor...';
            
            // İleride buraya yönlendirme kodu ekleyeceğiz (örn: window.location.href = 'dashboard.html')
            console.log('Token kaydedildi:', data.token);
        } else {
            errorMessage.style.color = '#dc3545';
            errorMessage.textContent = 'Kullanıcı adı veya şifre hatalı!';
        }
    } catch (error) {
        errorMessage.style.color = '#dc3545';
        errorMessage.textContent = 'Sunucuya bağlanılamadı. Backend çalışıyor mu?';
    }
});