let registerMode = false;


function switchMode(){
    if(!registerMode) {
        const confirmPassword = document.createElement("input");
        confirmPassword.setAttribute("type", "password");
        confirmPassword.setAttribute("name", "confirmPassword");
        confirmPassword.setAttribute("id", "confirmPassword");
        confirmPassword.setAttribute("placeholder", "Confirm Password")
        confirmPassword.setAttribute("class", "input");
        document.getElementById("container").insertBefore(confirmPassword, document.getElementById("errorOutput"));
        document.getElementById("login").innerHTML = "Register";
        document.getElementById("register").innerHTML = "Already have an account? ";
        const boldLogIn = document.createElement("b")
        boldLogIn.innerHTML = "Login";
        document.getElementById("register").appendChild(boldLogIn);

        registerMode = true;
    } else {
        document.getElementById("login").innerHTML = "Login";
        document.getElementById("register").innerHTML = "Register";
        document.getElementById("confirmPassword").remove();
        registerMode = false;
    }
}
function validateLoginForm(){
    let email = document.getElementById("email");
    let password = document.getElementById("password");
    let confirmPassword = document.getElementById("confirmPassword");
    let errorOutput = document.getElementById("errorOutput");

    if(email.value === ""){
        errorOutput.innerHTML = "E-mail cannot be empty";

        return;
    }

    if(password.value === "" && !registerMode){
        errorOutput.innerHTML = "Password cannot be empty";

        return;
    }

    //check if email is email format (regex)
    let mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(registerMode && !email.value.match(mailFormat)){
        errorOutput.innerHTML = "Invalid e-mail format";

        return;
    }

    if(registerMode && password.value.length < 8 && registerMode){
        errorOutput.innerHTML = "Password must be at least 8 characters";

        return;
    }

    if(registerMode && password.value !== confirmPassword.value){
        errorOutput.innerHTML = "Passwords do not match";

        return;
    }
    if(registerMode) {
        errorOutput.innerHTML = "";
        fetchUserSignup(email.value, password.value);
    } else {
        errorOutput.innerHTML = "";
        // if(!fetchUserLogin(email.value, password.value)){
        //     //don't go to next page
        //     alert("Your email or password may be incorrect.")
        // }
        // else{
        //     //go to next page
        //     console.log("Works")
        //     window.location.href = "./mainPage.html";
        // }

        fetchUserLogin(email.value, password.value)
            .then(loginReady => {console.log(localStorage.getItem("currentUser")); console.log("fish"); window.location.href = "./main"})
            .catch(error => {console.log(localStorage.getItem("currentUser")); alert("Your email or password may be incorrect.")});
    }

    return;
}
