    <div class="list-group list-group-flush">
        <div>
            <h2 class="list-group-item list-group-item-action h5 border-bottom-0">${folderName}</h2>
        </div>
        <a href="" class="list-group-item list-group-item-action" data-toggle="modal" data-target="#addFolder">Add folder</a>
        <a href="" class="list-group-item list-group-item-action" data-toggle="modal" data-target="#renameFolder">Rename folder</a>
        <a href="" class="list-group-item list-group-item-action" data-toggle="modal" data-target="#deleteFolder">Delete folder</a>
    </div>

    <div class="fixed-bottom">
        <form action="/file/add/${folderName}" method="post" enctype="multipart/form-data">
            <div class="input-group">
                <div class="custom-file">
                    <input type="file" class="custom-file-input" id="inputGroupFile04" aria-describedby="inputGroupFileAddon04" name="file">
                    <label class="custom-file-label" for="inputGroupFile04">Choose file</label>
                </div>
                <div class="input-group-append">
                    <button class="btn btn-outline-secondary" type="submit" id="inputGroupFileAddon04">Upload</button>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>

    <!-- Add Folder -->
    <div class="modal fade" id="addFolder" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
       <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Enter name folder</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="/folders/${folderName}" method="post">
                <div class="modal-body">
                    <input type="text" name="folderName" class="form-control"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                </form>
            </div>
        </div>
    </div>

    <!-- Rename Folder -->
    <div class="modal fade" id="renameFolder" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel1">Enter new name folder</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="/folders/rename/${folderName}" method="post">
                    <input type="hidden" name="_METHOD" value="PUT"/>
                    <div class="modal-body">
                        <input type="text" name="newFolderName" class="form-control" value="${folderName}"/>
                        <small class="form-text text-muted">You can't rename root folder.</small>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Folder -->
    <div class="modal fade" id="deleteFolder" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel2">Delete folder</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="/folders/delete/${folderName}" method="post">
                    <input type="hidden" name="_METHOD" value="DELETE"/>
                    <div class="modal-body">
                        <p>You can't delete root folder.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Delete folder</button>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                </form>
            </div>
        </div>
    </div>