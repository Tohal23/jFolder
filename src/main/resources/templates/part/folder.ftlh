    <#assign filesCount = files?size>
    <#assign foldersCount = foldersChild?size>
    <nav aria-label="breadcrumb m-0">
        <ol class="breadcrumb m-0">
            <#list foldersWay as folder>
                    <li class="breadcrumb-item rounded-0"><a href="/folders/${folder.name}">${folder.name}</a></li>
            </#list>
        </ol>
    </nav>
    <div class="list-group list-group-flush">
        <#if filesCount == 0 && foldersCount == 0>
            <a href="" class="list-group-item list-group-item-action h6">...Nothing</a>
        </#if>
        <#list foldersChild as folder>
            <div>
                <a href="/folders/${folder.name}" class="list-group-item list-group-item-action h6"><img src="img/folder.svg"> ${folder.name}</a>
            </div>
        </#list>

        <#list files as file>
            <div class="dropdown">
                <a href="#" class="list-group-item list-group-item-action h6 dropdown-toggle" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <img src="img/file.svg"> ${file.name}
                </a>
                <div class="dropdown-menu" style="width: 80%;" aria-labelledby="dropdownMenuLink">
                    <a class="dropdown-item" href="/file/get-file/${file.name}">Download</a>
                    <a class="dropdown-item" data-toggle="modal" data-target="#r_${file.id}" href="#">Rename</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" data-toggle="modal" data-target="#d_${file.id}" href="#">Delete</a>
                </div>
            </div>
        </#list>
    </div>

    <#list files as file>
        <!-- Delete Folder -->
        <div class="modal fade" id="d_${file.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel_${file.id}">Delete file</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="/file/delete/${file.name}" method="post">
                        <div class="modal-body">
                            <p>Delete file?</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Delete file</button>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    </form>
                </div>
            </div>
        </div>
    </#list>

    <#list files as file>
        <!-- Rename File -->
        <div class="modal fade" id="r_${file.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel1">Enter new name file</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form action="/file/rename/${file.name}" method="post">
                        <div class="modal-body">
                            <input type="text" name="newFileName" class="form-control" value="${file.name}"/>
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
    </#list>

