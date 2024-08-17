class Note {
    constructor(time, body, id){
        this.time = time;
        this.body = body;
        this.id = id;
    }
    getTime(){return this.time;}

    getBody(){return this.body;}

    getId(){return this.id;}
}

class User {
    constructor(email, password, token){
        this.email = email;
        this.password = password;
        this.token = token;
    }
    getEmail(){return this.email}

    getPassword(){return this.password}

    getToken(){return this.token}
}

class NoteFile {
    constructor(id, name, owner, canEdit, videoURL, notes){
        this.owner = owner;
        this.name = name;
        this.videoURL = videoURL;
        this.canEdit = canEdit;
        this.notes = notes;
        this.id = id;
    }
    getOwner(){return this.owner;}
    
    getName(){return this.name;}
    
    getVideoURL(){return this.videoURL;}
    
    getCanEdit(){return this.canEdit;}
    
    getNotes(){return this.notes;}
    
    getId(){return this.id;}
}

class FileName {
    constructor(id, name, owner, canEdit, lastModified){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.canEdit = canEdit;
        this.lastModified = lastModified;
    }
    getId(){return this.id;}
    
    getName(){return this.name;}

    getOwner(){return this.owner;}

    getCanEdit(){return this.canEdit;}
}