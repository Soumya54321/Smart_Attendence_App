function logout(){
    localStorage.clear();
}

function TeacherLoggedIn(){
    if(window.localStorage.getItem('email')!=null){
        window.location.href="/teacher/dashboard"
    }
    else{
        window.location.href="/teacher/login"
    }
}

function StudentLoggedIn(){
    if(window.localStorage.getItem('roll')!=null){
        window.location.href="/student/dashboard"
    }
    else{
        window.location.href="/student/login"
    }
}
function AdminLoggedIn(){
    if(window.localStorage.getItem('Id')==12345678){
        window.location.href="/admin/dashboard"
    }
    else{
        window.location.href="/admin/login"
    }
}