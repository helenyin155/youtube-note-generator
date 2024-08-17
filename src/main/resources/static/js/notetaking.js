let tabOpened = false;
let videoID = '';
let title = "";
let editingTitle = false;

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
        signOut.id = "signOut";
        signOut.onclick = function () {
            eraseCookie("token");
            window.location.href = "/";
        }
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

function eraseCookie(name) {
    document.cookie = name+'=; Max-Age=-99999999;';
}


let noteFile = new NoteFile();

function loadDataFromMainPage() {
    let username = document.getElementById("username");
    let editName = document.getElementById("editName");
    username.innerHTML = JSON.parse(localStorage.getItem("user")).email;
    // console.log(getTitle())
    let iframe = document.getElementById("videoFrame");
    videoID = localStorage.getItem("videoId");
    iframe.src = "https://www.youtube.com/embed/" + videoID + "?enablejsapi=1";

    // setInterval(function () {
    //     if (player !== undefined) {
    //
    //     }
    // }, 500);

    // console.log(getTitle())
    loadYouTubeAPI();

    noteFile.id = JSON.parse(localStorage.getItem("currentFile")).id;
    noteFile.videoURL = JSON.parse(localStorage.getItem("currentFile")).videoURL;
    noteFile.name = JSON.parse(localStorage.getItem("currentFile")).name;
    noteFile.notes = JSON.parse(localStorage.getItem("currentFile")).notes;


    setupListeners();

    const timeStampInterval = setInterval(setTime, 1000);
    const timelineInterval = setInterval(updateTimeline, 1000);

    const titleInterval = setInterval(function () {
        try{
            editName.innerHTML = getTitle();
            if (editName.innerHTML === getTitle()) {
                title = getTitle();

                loadAllNotes() //change to the file_id or something of the loaded video

                document.getElementById("loading").remove();
                document.getElementById("loadingbackground").remove();

                clearInterval(titleInterval);
            }
        } catch (e) {} //do nothing since getTitle() will throw an error if the video hasn't loaded yet
    }, 1);
    loadAllNotes();
}
function boldText(){
    document.execCommand("Bold", true, null);
}
function italicizeText(){
    document.execCommand("Italic", false, null);
}
function underlineText(){
    document.execCommand("Underline", false, null);
}
function listText(){
    document.execCommand("InsertUnorderedList", false, null);
}

function setupListeners(){
    let editName = document.getElementById("editName");
    editName.addEventListener('focus', function (){
        title = editName.innerHTML; //set title to currently edited title
        editingTitle = true;
    });
    editName.addEventListener('blur', function (){
        if (editingTitle) {
            title = editName.innerHTML; //if clicked off and was previously editing, set title to new title
            editingTitle = false; //set editing to false
            if (title === "" || title === "<br>") {
                title = "Untitled";
            }
        }
        editName.innerHTML = title;
    });
}

function returnToMainPage(){
    saveNote();
    writeToFile();
    window.location.href = "/main";
}
function addNoteBar(time, id){
    let progressBar = document.getElementById("myProgress");
    let bar = document.createElement("button");
    bar.className = "noteBar"; //100% of the height of the div
    bar.style.left = calculateTimePercentage(time)*100 + "%";
    bar.onclick = function (){
        if(bar.classList.contains("noteBarSelected")){
            return
        }
        unselectOtherNotes();
        bar.classList.add("noteBarSelected");
        openNote(id, time);
    }
    progressBar.insertBefore(bar, document.getElementById("myBar"));
}
function unselectOtherNotes(){
    let notes = document.getElementsByClassName("noteBar");
    for (let i = 0; i < notes.length; i++) {
        notes[i].classList.remove("noteBarSelected");
    }
}

function deleteAllNotes(){
    let bar = document.getElementById("myProgress");
    while(bar.childElementCount > 1){
        bar.removeChild(bar.children[0]);
    }
}

function loadAllNotes(/*videoDatabaseId*/){
    deleteAllNotes();
    //something edward
    noteFile.notes.forEach(function (note) {
        addNoteBar(note.time, note.id);
    })
}

function getNoteBody(noteId) {
    let a = ""
    noteFile.notes.forEach(function (note) {
        console.log(note.id + " " + note.body);
        console.log(noteId);
        if (note.id === noteId) {
            a = note.body;
        }
    });
    console.log("/n");
    return a;
}

function openNote(noteId, time){ //remove time
    // alert("open note " + noteId);
    //open note
    getNoteById(noteId)
    document.getElementById("noteArea").innerHTML = currentNote.body;
    goToTime(time);
}

