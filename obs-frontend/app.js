const BASE_URL = 'http://localhost:8080';
let currentToken = localStorage.getItem('accessToken') || '';
let currentRole = localStorage.getItem('userRole') || '';

// Global state for updates
let updateTarget = {
    type: '',
    id: null
};

// --- EKRAN GEÇİŞ FONKSİYONLARI ---
function showLoginType() {
    document.getElementById('login-type-container').classList.remove('hidden');
    document.getElementById('role-selection-container').classList.add('hidden');
    document.getElementById('login-container').classList.add('hidden');
    document.getElementById('dashboard-container').classList.add('hidden');
}

function showLoginRoleSelection() {
    document.getElementById('login-type-container').classList.add('hidden');
    document.getElementById('role-selection-container').classList.remove('hidden');
}

function goBackToLoginType() {
    showLoginType();
}

function showLoginForm(role) {
    document.getElementById('login-type-container').classList.add('hidden');
    document.getElementById('role-selection-container').classList.add('hidden');
    document.getElementById('login-container').classList.remove('hidden');

    document.getElementById('login-role').value = role;

    let title = "Giriş Paneli";
    if(role === 'admin') title = "Yönetici Girişi";
    if(role === 'teacher') title = "Öğretmen Girişi";
    if(role === 'student') title = "Öğrenci Girişi";
    document.getElementById('login-title').textContent = title;
}

function showDashboard() {
    document.getElementById('login-container').classList.add('hidden');
    document.getElementById('login-type-container').classList.add('hidden');
    document.getElementById('role-selection-container').classList.add('hidden');
    document.getElementById('dashboard-container').classList.remove('hidden');

    document.getElementById('admin-panel').classList.add('hidden');
    document.getElementById('teacher-panel').classList.add('hidden');
    document.getElementById('student-panel').classList.add('hidden');

    if (currentRole === 'admin') {
        document.getElementById('admin-panel').classList.remove('hidden');
        showSection('admin', 'announcements');
    } else if (currentRole === 'teacher') {
        document.getElementById('teacher-panel').classList.remove('hidden');
        showSection('teacher', 'announcements');
    } else if (currentRole === 'student') {
        document.getElementById('student-panel').classList.remove('hidden');
        showSection('student', 'announcements');
    } else {
        alert("Geçersiz veya eksik rol tespit edildi. Lütfen tekrar giriş yapın.");
        logout();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    if (currentToken && currentRole) {
        showDashboard();
    } else {
        showLoginType();
    }
});


// --- LOGIN İŞLEMİ ---
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const selectedRole = document.getElementById('login-role').value;
        const loginError = document.getElementById('login-error');

        loginError.textContent = '';

        try {
            const response = await fetch(`${BASE_URL}/authenticate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                currentToken = data.accessToken;
                currentRole = selectedRole;

                localStorage.setItem('accessToken', currentToken);
                localStorage.setItem('userRole', currentRole);
                showDashboard();
            } else {
                loginError.textContent = 'Kullanıcı adı veya şifre hatalı!';
            }
        } catch (error) {
            loginError.textContent = 'Sunucuya bağlanılamadı.';
            console.error('Login error:', error);
        }
    });
}

function logout() {
    currentToken = '';
    currentRole = '';
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRole');
    showLoginType();
}
const logoutBtn = document.getElementById('logout-btn');
if (logoutBtn) logoutBtn.addEventListener('click', logout);


// --- MENÜ GEÇİŞLERİ ---
function showSection(rolePrefix, sectionName) {
    const allSections = document.querySelectorAll('.content-section');
    allSections.forEach(sec => sec.classList.add('hidden'));

    const targetSectionId = `${rolePrefix}-${sectionName}-section`;
    const activeSection = document.getElementById(targetSectionId);
    if (activeSection) {
        activeSection.classList.remove('hidden');
        activeSection.classList.add('active');
    }

    if (sectionName === 'announcements') fetchAnnouncements(rolePrefix);
    if (sectionName === 'faculties') fetchFaculties(rolePrefix);
    if (sectionName === 'departments') fetchDepartments(rolePrefix);
    if (sectionName === 'teachers') fetchTeachers(rolePrefix);
    if (sectionName === 'students') fetchStudents(rolePrefix);
    if (sectionName === 'classrooms') fetchClassrooms(rolePrefix);
    if (sectionName === 'semesters') fetchSemesters(rolePrefix);
    if (sectionName === 'courses') fetchCourses(rolePrefix);
}


// --- API İSTEKLERİ (GET) ---

async function fetchAnnouncements(rolePrefix) {
    const listId = `${rolePrefix}-announcements-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        // Backend urlsi getAll yerine all olduysa diye:
        const response = await fetch(`${BASE_URL}/announcement/all`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Duyuru yok.</li>'; return; }
            data.forEach(ann => {
                const li = document.createElement('li');
                const date = ann.createdDate ? new Date(ann.createdDate).toLocaleString('tr-TR') : 'Tarih Bilgisi Yok';
                li.innerHTML = `
                    <strong>${ann.title}</strong>
                    <p>${ann.content}</p>
                    <span>Yazan: ${ann.author || 'Admin'} | Tarih: ${date}</span>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditAnnouncement(${ann.id}, '${ann.title}', '${ann.content}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('announcement', ${ann.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchFaculties(rolePrefix) {
    const listId = `${rolePrefix}-faculties-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/faculty/getAll`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Fakülte yok.</li>'; return; }
            data.forEach(fac => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${fac.name}</strong>
                    <p>Dekan: ${fac.dean || '-'} | Adres: ${fac.address || '-'}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditFaculty(${fac.id}, '${fac.name}', '${fac.dean}', '${fac.address}', '${fac.year_of_establishment}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('faculty', ${fac.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchDepartments(rolePrefix) {
    const listId = `${rolePrefix}-departments-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/department/getAll`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Bölüm yok.</li>'; return; }
            data.forEach(dep => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${dep.name}</strong>
                    <p>Başkan: ${dep.head_of_department || '-'} | Kontenjan: ${dep.quota || '-'}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditDepartment(${dep.id}, '${dep.name}', '${dep.quota}', '${dep.head_of_department}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('department', ${dep.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchTeachers(rolePrefix) {
    const listId = `${rolePrefix}-teachers-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/teacher/all`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Öğretmen yok.</li>'; return; }
            data.forEach(teach => {
                const li = document.createElement('li');
                const deptName = teach.department ? teach.department.name : 'Bölüm Yok';
                const regNo = teach.user ? teach.user.username : 'Belirtilmemiş';
                li.innerHTML = `
                    <strong>${teach.firstName} ${teach.lastName} (Sicil: ${regNo})</strong>
                    <p>Bölüm: ${deptName}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditTeacher(${teach.id}, '${teach.firstName}', '${teach.lastName}', '${regNo}', '${teach.department ? teach.department.id : ''}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('teacher', ${teach.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchStudents(rolePrefix) {
    const listId = `${rolePrefix}-students-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/student/getAll`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Öğrenci yok.</li>'; return; }
            data.forEach(stu => {
                const li = document.createElement('li');
                const deptName = stu.department ? stu.department.name : 'Bölüm Yok';
                li.innerHTML = `
                    <strong>${stu.firstName} ${stu.lastName} (${stu.studentNumber})</strong>
                    <p>Bölüm: ${deptName}</p>
                    <span>Email: ${stu.email || '-'} | Tel: ${stu.telNumber || '-'}</span>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditStudent(${stu.id}, '${stu.firstName}', '${stu.lastName}', '${stu.studentNumber}', '${stu.email}', '${stu.telNumber}', '${stu.department ? stu.department.id : ''}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('student', ${stu.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchClassrooms(rolePrefix) {
    const listId = `${rolePrefix}-classrooms-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/classroom/all`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Sınıf yok.</li>'; return; }
            data.forEach(cls => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${cls.roomNumber}</strong>
                    <p>Kapasite: ${cls.capacity}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditClassroom(${cls.id}, '${cls.roomNumber}', '${cls.capacity}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('classroom', ${cls.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchSemesters(rolePrefix) {
    const listId = `${rolePrefix}-semesters-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/semester/all`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Dönem yok.</li>'; return; }
            data.forEach(sem => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${sem.term}</strong>
                    <p>Aktif mi: ${sem.isActive ? 'Evet' : 'Hayır'}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditSemester(${sem.id}, '${sem.term}', ${sem.isActive})" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('semester', ${sem.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}

async function fetchCourses(rolePrefix) {
    const listId = `${rolePrefix}-courses-list`;
    const list = document.getElementById(listId);
    if(!list) return;
    list.innerHTML = '<li>Yükleniyor...</li>';

    try {
        const response = await fetch(`${BASE_URL}/course/all`, {
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if (response.ok) {
            const data = await response.json();
            list.innerHTML = '';
            if (data.length === 0) { list.innerHTML = '<li>Ders yok.</li>'; return; }
            data.forEach(crs => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${crs.code} - ${crs.name}</strong>
                    <p>Kredi: ${crs.credit} | AKTS: ${crs.akts} | Kontenjan: ${crs.quota}</p>
                    ${rolePrefix === 'admin' ? `
                    <div class="list-actions">
                        <button onclick="openEditCourse(${crs.id}, '${crs.code}', '${crs.name}', '${crs.credit}', '${crs.akts}', '${crs.quota}')" class="btn btn-warning">Düzenle</button>
                        <button onclick="deleteEntity('course', ${crs.id})" class="btn btn-danger">Sil</button>
                    </div>` : ''}
                `;
                list.appendChild(li);
            });
        } else { handleAuthError(response); }
    } catch (error) { list.innerHTML = '<li>Hata oluştu!</li>'; }
}


// --- API İSTEKLERİ (POST - EKLEME İŞLEMLERİ) ---

async function addAnnouncement() {
    const title = document.getElementById('ann-title').value;
    const content = document.getElementById('ann-content').value;

    if(!title || !content ) { alert("Lütfen tüm alanları doldurun."); return; }

    try {
        const response = await fetch(`${BASE_URL}/announcement/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ title, content, author: 'Yönetici' }) // Teacher id falan kalktı diye author gönderiyoruz
        });

        if (response.ok) {
            alert("Duyuru başarıyla eklendi!");
            document.getElementById('ann-title').value = '';
            document.getElementById('ann-content').value = '';
            fetchAnnouncements('admin');
        } else { alert("Duyuru eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addTeacherAnnouncement() {
    const title = document.getElementById('t-ann-title').value;
    const content = document.getElementById('t-ann-content').value;

    if(!title || !content) { alert("Lütfen tüm alanları doldurun."); return; }

    try {
        const response = await fetch(`${BASE_URL}/announcement/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ title, content, author: 'Öğretmen' })
        });

        if (response.ok) {
            alert("Duyuru başarıyla yayınlandı!");
            document.getElementById('t-ann-title').value = '';
            document.getElementById('t-ann-content').value = '';
            fetchAnnouncements('teacher');
        } else { alert("Duyuru eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addFaculty() {
    const name = document.getElementById('fac-name').value;
    const dean = document.getElementById('fac-dean').value;
    const address = document.getElementById('fac-address').value;
    const year_of_establishment = document.getElementById('fac-establishment').value;

    if(!name) { alert("Fakülte adı zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/faculty/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ name, dean, address, year_of_establishment })
        });

        if (response.ok) {
            alert("Fakülte eklendi!");
            document.getElementById('fac-name').value = '';
            document.getElementById('fac-dean').value = '';
            document.getElementById('fac-address').value = '';
            document.getElementById('fac-establishment').value = '';
            fetchFaculties('admin');
        } else { alert("Fakülte eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addDepartment() {
    const name = document.getElementById('dep-name').value;
    const quota = document.getElementById('dep-quota').value;
    const head_of_department = document.getElementById('dep-head').value;

    if(!name) { alert("Bölüm adı zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/department/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ name, quota: parseInt(quota), head_of_department })
        });

        if (response.ok) {
            alert("Bölüm eklendi!");
            document.getElementById('dep-name').value = '';
            document.getElementById('dep-quota').value = '';
            document.getElementById('dep-head').value = '';
            fetchDepartments('admin');
        } else { alert("Bölüm eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addTeacher() {
    const firstName = document.getElementById('teach-fn').value;
    const lastName = document.getElementById('teach-ln').value;
    const registrationNumber = document.getElementById('teach-reg-no').value;
    const departmentId = document.getElementById('teach-dep-id').value;

    if(!firstName || !lastName || !registrationNumber || !departmentId) { alert("Tüm alanlar zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/teacher/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            // userId property is removed. We use registrationNumber now.
            body: JSON.stringify({ firstName, lastName, registrationNumber, departmentId: parseInt(departmentId) })
        });

        if (response.ok) {
            alert("Öğretmen eklendi! Sicil numarası ile giriş yapabilir.");
            document.getElementById('teach-fn').value = '';
            document.getElementById('teach-ln').value = '';
            document.getElementById('teach-reg-no').value = '';
            document.getElementById('teach-dep-id').value = '';
            fetchTeachers('admin');
        } else {
            const errorMsg = await response.text();
            alert("Öğretmen eklenemedi: " + errorMsg);
        }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addStudent() {
    const firstName = document.getElementById('stu-fn').value;
    const lastName = document.getElementById('stu-ln').value;
    const studentNumber = document.getElementById('stu-no').value;
    const email = document.getElementById('stu-email').value;
    const telNumber = document.getElementById('stu-tel').value;
    const departmentId = document.getElementById('stu-dep-id').value;

    if(!firstName || !lastName || !studentNumber || !departmentId) { alert("Zorunlu alanları doldurun."); return; }

    try {
        const response = await fetch(`${BASE_URL}/student/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({
                firstName,
                lastName,
                studentNumber,
                email,
                telNumber,
                departmentId: parseInt(departmentId)
            })
        });

        if (response.ok) {
            alert("Öğrenci eklendi! Öğrenci numarası ile giriş yapabilir.");
            document.getElementById('stu-fn').value = '';
            document.getElementById('stu-ln').value = '';
            document.getElementById('stu-no').value = '';
            document.getElementById('stu-email').value = '';
            document.getElementById('stu-tel').value = '';
            document.getElementById('stu-dep-id').value = '';
            fetchStudents('admin');
        } else {
            const errorMsg = await response.text();
            alert("Öğrenci eklenemedi: " + errorMsg);
        }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addClassroom() {
    const roomNumber = document.getElementById('cls-room').value;
    const capacity = document.getElementById('cls-cap').value;

    if(!roomNumber || !capacity) { alert("Tüm alanlar zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/classroom/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ roomNumber, capacity: parseInt(capacity) })
        });

        if (response.ok) {
            alert("Sınıf eklendi!");
            document.getElementById('cls-room').value = '';
            document.getElementById('cls-cap').value = '';
            fetchClassrooms('admin');
        } else { alert("Sınıf eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addSemester() {
    const term = document.getElementById('sem-term').value;
    const isActive = document.getElementById('sem-active').checked;

    if(!term) { alert("Dönem adı zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/semester/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ term, isActive })
        });

        if (response.ok) {
            alert("Dönem eklendi!");
            document.getElementById('sem-term').value = '';
            document.getElementById('sem-active').checked = true;
            fetchSemesters('admin');
        } else { alert("Dönem eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

async function addCourse() {
    const code = document.getElementById('crs-code').value;
    const name = document.getElementById('crs-name').value;
    const credit = document.getElementById('crs-credit').value;
    const akts = document.getElementById('crs-akts').value;
    const quota = document.getElementById('crs-quota').value;

    if(!code || !name) { alert("Ders kodu ve adı zorunludur."); return; }

    try {
        const response = await fetch(`${BASE_URL}/course/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify({ code, name, credit, akts: parseInt(akts), quota: parseInt(quota) })
        });

        if (response.ok) {
            alert("Ders eklendi!");
            document.getElementById('crs-code').value = '';
            document.getElementById('crs-name').value = '';
            document.getElementById('crs-credit').value = '';
            document.getElementById('crs-akts').value = '';
            document.getElementById('crs-quota').value = '';
            fetchCourses('admin');
        } else { alert("Ders eklenemedi."); }
    } catch (error) { alert("Sunucu hatası."); }
}

// --- DELETE ---
async function deleteEntity(type, id) {
    console.log(`Deleting ${type} with ID: ${id}`);
    if (!id || id === 'undefined') {
        alert("Hata: Kayıt ID'si bulunamadı. Lütfen sayfayı yenileyip tekrar deneyin.");
        return;
    }

    if(!confirm("Bu kaydı silmek istediğinizden emin misiniz?")) return;

    let endpoint = '';
    switch(type) {
        case 'announcement': endpoint = `/announcement/delete/${id}`; break;
        case 'faculty': endpoint = `/faculty/delete/${id}`; break;
        case 'department': endpoint = `/department/delete/${id}`; break;
        case 'teacher': endpoint = `/teacher/delete/${id}`; break;
        case 'student': endpoint = `/student/delete/${id}`; break;
        case 'classroom': endpoint = `/classroom/delete/${id}`; break;
        case 'semester': endpoint = `/semester/delete/${id}`; break;
        case 'course': endpoint = `/course/delete/${id}`; break;
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${currentToken}` }
        });
        if(response.ok) {
            alert("Silindi.");
            refreshSection(type);
        } else {
            alert("Silinemedi. (Bağlı veriler olabilir)");
        }
    } catch (e) { alert("Sunucu hatası"); }
}

function refreshSection(type) {
    if (type === 'announcement') fetchAnnouncements('admin');
    if (type === 'faculty') fetchFaculties('admin');
    if (type === 'department') fetchDepartments('admin');
    if (type === 'teacher') fetchTeachers('admin');
    if (type === 'student') fetchStudents('admin');
    if (type === 'classroom') fetchClassrooms('admin');
    if (type === 'semester') fetchSemesters('admin');
    if (type === 'course') fetchCourses('admin');
}

// --- UPDATE (MODAL) ---

function openEditModal(title) {
    document.getElementById('edit-modal-title').textContent = title;
    document.getElementById('edit-modal').classList.remove('hidden');
}

function closeEditModal() {
    document.getElementById('edit-modal').classList.add('hidden');
    document.getElementById('edit-modal-form').innerHTML = '';
}

function openEditAnnouncement(id, title, content) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'announcement', id };
    openEditModal("Duyuru Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Başlık</label>
        <input type="text" id="edit-ann-title" value="${title}" class="form-input">
        <label>İçerik</label>
        <textarea id="edit-ann-content" class="form-input">${content}</textarea>
    `;
}

function openEditFaculty(id, name, dean, address, year) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'faculty', id };
    openEditModal("Fakülte Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Fakülte Adı</label>
        <input type="text" id="edit-fac-name" value="${name}" class="form-input">
        <label>Dekan</label>
        <input type="text" id="edit-fac-dean" value="${dean !== 'null' ? dean : ''}" class="form-input">
        <label>Adres</label>
        <input type="text" id="edit-fac-address" value="${address !== 'null' ? address : ''}" class="form-input">
        <label>Kuruluş Yılı</label>
        <input type="text" id="edit-fac-year" value="${year !== 'null' ? year : ''}" class="form-input">
    `;
}

function openEditDepartment(id, name, quota, head) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'department', id };
    openEditModal("Bölüm Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Bölüm Adı</label>
        <input type="text" id="edit-dep-name" value="${name}" class="form-input">
        <label>Kontenjan</label>
        <input type="number" id="edit-dep-quota" value="${quota !== 'null' ? quota : ''}" class="form-input">
        <label>Bölüm Başkanı</label>
        <input type="text" id="edit-dep-head" value="${head !== 'null' ? head : ''}" class="form-input">
    `;
}

function openEditTeacher(id, fn, ln, regNo, depId) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'teacher', id };
    openEditModal("Öğretmen Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Adı</label>
        <input type="text" id="edit-teach-fn" value="${fn}" class="form-input">
        <label>Soyadı</label>
        <input type="text" id="edit-teach-ln" value="${ln}" class="form-input">
        <label>Sicil Numarası</label>
        <input type="text" id="edit-teach-reg-no" value="${regNo}" class="form-input">
        <label>Bölüm ID</label>
        <input type="number" id="edit-teach-dep" value="${depId !== 'null' ? depId : ''}" class="form-input">
    `;
}

function openEditStudent(id, fn, ln, no, email, tel, depId) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'student', id };
    openEditModal("Öğrenci Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Adı</label>
        <input type="text" id="edit-stu-fn" value="${fn}" class="form-input">
        <label>Soyadı</label>
        <input type="text" id="edit-stu-ln" value="${ln}" class="form-input">
        <label>No</label>
        <input type="text" id="edit-stu-no" value="${no}" class="form-input">
        <label>Email</label>
        <input type="email" id="edit-stu-email" value="${email !== 'null' ? email : ''}" class="form-input">
        <label>Tel</label>
        <input type="text" id="edit-stu-tel" value="${tel !== 'null' ? tel : ''}" class="form-input">
        <label>Bölüm ID</label>
        <input type="number" id="edit-stu-dep" value="${depId !== 'null' ? depId : ''}" class="form-input">
    `;
}

function openEditClassroom(id, roomNumber, capacity) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'classroom', id };
    openEditModal("Sınıf Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Sınıf No</label>
        <input type="text" id="edit-cls-room" value="${roomNumber}" class="form-input">
        <label>Kapasite</label>
        <input type="number" id="edit-cls-cap" value="${capacity}" class="form-input">
    `;
}

function openEditSemester(id, term, isActive) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'semester', id };
    openEditModal("Dönem Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Dönem Adı</label>
        <input type="text" id="edit-sem-term" value="${term}" class="form-input">
        <label>Aktif mi?</label>
        <input type="checkbox" id="edit-sem-active" ${isActive ? 'checked' : ''}>
    `;
}

function openEditCourse(id, code, name, credit, akts, quota) {
    if (!id) { alert("ID eksik!"); return; }
    updateTarget = { type: 'course', id };
    openEditModal("Ders Güncelle");
    document.getElementById('edit-modal-form').innerHTML = `
        <label>Ders Kodu</label>
        <input type="text" id="edit-crs-code" value="${code}" class="form-input">
        <label>Ders Adı</label>
        <input type="text" id="edit-crs-name" value="${name}" class="form-input">
        <label>Kredi</label>
        <input type="text" id="edit-crs-credit" value="${credit}" class="form-input">
        <label>AKTS</label>
        <input type="number" id="edit-crs-akts" value="${akts}" class="form-input">
        <label>Kontenjan</label>
        <input type="number" id="edit-crs-quota" value="${quota}" class="form-input">
    `;
}

async function submitUpdate() {
    let body = {};
    let endpoint = '';

    if (!updateTarget.id) { alert("Güncellenecek ID bulunamadı."); return; }

    if (updateTarget.type === 'announcement') {
        const title = document.getElementById('edit-ann-title').value;
        const content = document.getElementById('edit-ann-content').value;
        body = { id: updateTarget.id, title, content };
        endpoint = `/announcement/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'faculty') {
        const name = document.getElementById('edit-fac-name').value;
        const dean = document.getElementById('edit-fac-dean').value;
        const address = document.getElementById('edit-fac-address').value;
        const year_of_establishment = document.getElementById('edit-fac-year').value;
        body = { id: updateTarget.id, name, dean, address, year_of_establishment };
        endpoint = `/faculty/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'department') {
        const name = document.getElementById('edit-dep-name').value;
        const quota = parseInt(document.getElementById('edit-dep-quota').value);
        const head_of_department = document.getElementById('edit-dep-head').value;
        body = { id: updateTarget.id, name, quota, head_of_department };
        endpoint = `/department/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'teacher') {
        const firstName = document.getElementById('edit-teach-fn').value;
        const lastName = document.getElementById('edit-teach-ln').value;
        const registrationNumber = document.getElementById('edit-teach-reg-no').value;
        const departmentId = parseInt(document.getElementById('edit-teach-dep').value);
        body = { firstName, lastName, registrationNumber, departmentId };
        endpoint = `/teacher/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'student') {
        const firstName = document.getElementById('edit-stu-fn').value;
        const lastName = document.getElementById('edit-stu-ln').value;
        const studentNumber = document.getElementById('edit-stu-no').value;
        const email = document.getElementById('edit-stu-email').value;
        const telNumber = document.getElementById('edit-stu-tel').value;
        const departmentId = parseInt(document.getElementById('edit-stu-dep').value);
        body = { firstName, lastName, studentNumber, email, telNumber, departmentId };
        endpoint = `/student/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'classroom') {
        const roomNumber = document.getElementById('edit-cls-room').value;
        const capacity = parseInt(document.getElementById('edit-cls-cap').value);
        body = { roomNumber, capacity };
        endpoint = `/classroom/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'semester') {
        const term = document.getElementById('edit-sem-term').value;
        const isActive = document.getElementById('edit-sem-active').checked;
        body = { term, isActive };
        endpoint = `/semester/update/${updateTarget.id}`;
    } else if (updateTarget.type === 'course') {
        const code = document.getElementById('edit-crs-code').value;
        const name = document.getElementById('edit-crs-name').value;
        const credit = document.getElementById('edit-crs-credit').value;
        const akts = parseInt(document.getElementById('edit-crs-akts').value);
        const quota = parseInt(document.getElementById('edit-crs-quota').value);
        body = { code, name, credit, akts, quota };
        endpoint = `/course/update/${updateTarget.id}`;
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${currentToken}` },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            alert("Güncellendi!");
            closeEditModal();
            refreshSection(updateTarget.type);
        } else {
            const errText = await response.text();
            alert("Güncelleme başarısız: " + errText);
        }
    } catch (e) { alert("Sunucu hatası."); }
}

function handleAuthError(response) {
    if (response.status === 401 || response.status === 403) {
        alert("Oturum süreniz dolmuş veya yetkiniz yok.");
        logout();
    }
}