let tabOpened = false;
// const jwtToken = document.cookie
//     .split("; ")
//     .find(row => row.startsWith("token="))
//     .split("=")[1];
// console.log(jwtToken);
function toggleTab(){
    if(!tabOpened){
        /*
        add
        <div class="tab" id="accountOptions">
            <button class="tablinks" onclick="doSomething()">Change Password</button>
            <button class="tablinks" onclick="doSomething()">Sign Out</button>
            <button class="tablinks" onclick="doSomething()">Switch Account</button>
        </div>

        before element with username ID

         */
        let container = document.getElementsByClassName("containerRight")[0];

        tabOpened = true;
        let tab = document.createElement("div");
        tab.className = "tab";
        tab.id = "accountOptions";
        let changePassword = document.createElement("button");
        changePassword.className = "tablinks";
        changePassword.innerHTML = "Change Password";
        let signOut = document.createElement("button");
        signOut.className = "tablinks";
        signOut.innerHTML = "Sign Out";
        let switchAccount = document.createElement("button");
        switchAccount.className = "tablinks";
        switchAccount.innerHTML = "Switch Account";
        tab.appendChild(changePassword);
        tab.appendChild(signOut);
        tab.appendChild(switchAccount);
        container.appendChild(tab);
    } else {
        /*
        remove
        <div class="tab" id="accountOptions">
            <button class="tablinks" onclick="doSomething()">Change Password</button>
            <button class="tablinks" onclick="doSomething()">Sign Out</button>
            <button class="tablinks" onclick="doSomething()">Switch Account</button>
        </div>

        before element with username ID

         */
        tabOpened = false;
        document.getElementById("accountOptions").remove();
    }
}

function getIdFromUrl(url){
    var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#&?]*).*/;
    var match = url.match(regExp);
    return (match&&match[7].length==11)? match[7] : false;
}

function validVideoId() {
    let videoLink = document.getElementById("videoLink").value;
    let videoId = getIdFromUrl(videoLink);
    let img = new Image();
    if (videoId !== false) {
        img.src = "https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
    } else {
        document.getElementById("errorOutput").innerHTML = "Invalid video URL format.";
        return;
    }

    img.onload = function () {
        checkThumbnail(img.width);
    }

    function checkThumbnail(width) {
        //HACK a mq thumbnail has width of 320.
        //if the video does not exist(therefore thumbnail don't exist), a default thumbnail of 120 width is returned.

        if (width == 120) {
            document.getElementById("errorOutput").innerHTML = "Invalid video URL.";
        } else {
            //link to notetaking.html
            localStorage.setItem("videoId", videoId);
            createNewFile(videoLink);
            setInterval(function () {
                if (localStorage.getItem("currentFile") !== null)
                    window.location.href = "/notes";
            }, 500);
            //create file
        }
    }
}
function loadFile(id) {
    getFile(id);
    setInterval(function () {
        if (localStorage.getItem("currentFile") !== null)
            window.location.href = "/notes";
    }, 500);
}

function createNewFile(videoURL){
    createFileMain("Untitled", videoURL)
}

function createFileMain(name, videoURL){
    fetch("http://localhost:8080/file/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
            name: name,
            videoURL: videoURL
        })
    }).then(response => response.json())
        .then(data => {currentFile = new NoteFile(data.id, name, JSON.parse(localStorage.getItem("user")).email, true, videoURL, []); localStorage.setItem("currentFile", JSON.stringify(currentFile));})
        .catch(error => console.log(error))
}

function loadUsername(){
    let username = document.getElementById("username");
    username.innerHTML = JSON.parse(localStorage.getItem("user")).email;

    document.getElementById("loading").remove();
    document.getElementById("loadingbackground").remove();

}

function makeTable(){
    var table = document.getElementById("fileTable");
    getAllFiles();
    JSON.parse(localStorage.getItem("fileNames")).forEach(element => {
        var row = table.insertRow();
        row.id=element.id;
        row.onclick=function(){ loadFile(element.id);  };
        row.style.cursor = "pointer";
        var name = row.insertCell(0);
        var date = row.insertCell(1);
        name.innerHTML = element.name;
        date.innerHTML = element.lastModified;
    })
}

// function getAllFiles(){
//     fileNames.length = 0;
//     fetch("http://localhost:8080/file/getall", {
//         method: "GET",
//         headers: {
//             "authorization": `Bearer ${jwtToken}`
//         }
//     }).then(response => response.json())
//     .then(data => {
//         data.notes.forEach(element => {
//         const fileName = new FileName(element[0], element[1], element[2], element[3]);
//         fileNames.push(fileName);
//         // localStorage.setItem("fileNames", fileNames)
//     })
// })
//     .catch(error => console.log(error))
//     return fileNames;
// }