//remember to make script type module
//might need to alter isSuccessful?

const fileNames = [];
const jwtToken = document.cookie
    .split("; ")
    .find(row => row.startsWith("token="))
    .split("=")[1];

function getAllFiles(){
    fileNames.length = 0;
    fetch("http://localhost:8080/file/getall", {
        method: "GET",
        headers: {
            "authorization": `Bearer ${jwtToken}`
        }
    }).then(response => response.json())
    .then(data => {
        data.fileList.forEach(element => {
        const fileName = new FileName(element.id, element.name, element.owner, element.canEdit, element.lastModified);
        fileNames.push(fileName);
        // localStorage.setItem("fileNames", fileNames)
    })
        localStorage.setItem("fileNames", JSON.stringify(fileNames));

    })
    .catch(error => console.log(error))
    setInterval(function () {
        if (JSON.parse(localStorage.getItem("fileNames")) != null){
            clearInterval(this);
            return fileNames;
        }
    }, 500)
}
let currentFile = JSON.parse(localStorage.getItem("currentFile"));
let currentNote = new Note(null, null, null);
console.log(currentFile);

function getFile(id){
    fetch("http://localhost:8080/file/get/" + id, {
        method: "GET",
        headers: {
            "authorization": `Bearer ${jwtToken}`
        }
    }).then(response => response.json())
    .then(data => {currentFile.id = data.id;
        currentFile.name = data.name;
        currentFile.owner = data.owner;
        currentFile.notes = data.notes;
        currentFile.videoURL = data.videoURL;
    console.log(currentFile)
        localStorage.setItem("currentFile", JSON.stringify(currentFile));
        localStorage.setItem("videoId", getIdFromUrl(data.videoURL));
    })
    .catch(error => console.log(error))
    // localStorage.setItem("currentFile", currentFile);
}

function writeToFile(){
    // let currentFile = localStorage.getItem("currentFile")
    fetch("http://localhost:8080/file/update", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
            id: currentFile.id,
            name: document.getElementById("editName").innerHTML,
        })
    }).then(response => response.json())
    .then(data => data.isSuccessful = true) //for updating title
    .catch(error => console.log(error))
}

function createFile(name, videoURL){
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
    .then(data => {currentFile = new NoteFile(data.id, name, owner, true, videoURL, [])})
    .catch(error => console.log(error))
}

function deleteFile(){

    // let currentFile = localStorage.getItem("currentFile")
    fetch("http://localhost:8080/file/delete/" + currentFile.id, {
        method: "GET",
        headers: {
            "authorization": `Bearer ${jwtToken}`
        }
    }).then(response => response.json())
    .then(data => data.isSuccessful = true)
    .catch(error => console.log(error))
    // localStorage.removeItem("currentFile")
}

function addNote(time, body){
    // let currentFile = localStorage.getItem("currentFile")
    fetch("http://localhost:8080/file/note/add/" + currentFile.id, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
            time: time,
            body: body
        })
    }).then(response => response.json())
    .then(data => {
        currentNote.time = time;
        currentNote.id = data.id;
        currentNote.body = body;
        noteFile.notes.push(new Note(time, body, data.id));
        loadAllNotes()
    })
    .catch(error => console.log(error))
    // localStorage.setItem("currentNote", JSON.stringify(currentNote))
}

function replaceNote(){
    console.log(currentNote)
    // currentNote = JSON.parse(localStorage.getItem("currentNote"))
    fetch("http://localhost:8080/file/note/replace", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
            id: currentNote.id,
            time: currentNote.time,
            body: currentNote.body
        })
    }).then(response => response.json())
    .then(data => {
        data.isSuccessful = true;
        for (let i = 0; i < noteFile.notes.length; i++){
            if (noteFile.notes[i].id === currentNote.id){
                noteFile.notes[i].body = currentNote.body;
                console.log("count");
            }
        }
        loadAllNotes()
    })
    .catch(error => console.log(error))
}

function deleteNote(noteId){
    fetch("http://localhost:8080/file/note/delete/" + noteId, {
        method: "DELETE",
        headers: {
            "authorization": `Bearer ${jwtToken}`
        }
    }).then(data => {
        data.isSuccessful = true;
        for (let i = 0; i < noteFile.notes.length; i++){
            if (noteFile.notes[i].id === noteId){
                noteFile.notes.splice(i, 1);
            }
        }
    })
    .catch(error => console.log(error))
}

function getNoteById(noteId){
    fetch("http://localhost:8080/file/note/get/" + noteId, {
        method: "GET",
        headers: {
            "authorization": `Bearer ${jwtToken}`
        }
    }).then(response => response.json())
    .then(data => {
        currentNote.id = data.id;
        currentNote.time = data.time;
        currentNote.body = data.body;
    })
        .catch(error => console.log(error))
}

function shareFile(email, fileId, canEdit){
    fetch("http://localhost:8080/file/share", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify({
            email: email,
            id: fileId,
            canEdit: canEdit
        })
    }).then(response => response.json())
        .then(data => data.isSuccessful = true)
        .catch(error => console.log(error))
}

function setTime(){
    const time = returnTime()
    let minutes = Math.floor(time / 60);
    let seconds = Math.floor(time % 60)
    document.getElementById("changingTimeStamp").innerHTML = minutes + (seconds < 10 ? ":0" : ":") + seconds;
}

function updateTimeline(){
    const percentage = currentTimePercentage() * 100;
    document.getElementById("myBar").style.width = percentage + "%";
}

function saveNote(){
    currentNote.time = returnInitTime();
    currentNote.body = document.getElementById("noteArea").innerHTML;
    // localStorage.setItem("currentNote", JSON.stringify(currentNote))
    if (currentNote.body === "") {
        deleteCurrentNote();
    } else {
        replaceNote();
    }
}

function returnInitTime(){
    let time = document.getElementById("timeStamp").innerHTML;
    let minutes = parseInt(time.substring(0, time.indexOf(":")));
    let seconds = parseInt(time.substring(time.indexOf(":") + 1));
    return minutes * 60 + seconds;
}

function newNote(){
    // saveNote();
    document.getElementById("noteArea").innerHTML = "";
    document.getElementById("timeStamp").innerHTML = document.getElementById("changingTimeStamp").innerHTML;
    addNote(returnTime(), "");
}

function openNote(index){
    currentNote.time = notes[index].time;
    currentNote.body = notes[index].body;
    currentNote.id = notes[index].id;
    localStorage.setItem("currentNote", JSON.stringify(currentNote))
    minutes = Math.floor(currentNote.time/60);
    seconds = Math.floor(currentNote.time%60)
    document.getElementById("changingTimeStamp").innerHTML = minutes + (seconds < 10 ? ":0" : ":") + seconds;
}

function displayAllFiles(){
    //display all files and their respective name, owner, and whether or not they can edit
    fileNames.forEach(element => {
        console.log(element.owner)
        console.log(element.name)
        console.log(element.canEdit)
    });
}

function deleteCurrentNote(){
    deleteNote(currentNote.id)
    noteFile.notes.pop();
    localStorage.removeItem("currentNote")
}
function saveFile(){
    writeToFile()
}