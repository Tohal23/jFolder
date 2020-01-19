
    <nav aria-label="breadcrumb m-0">
        <ol class="breadcrumb m-0">
            <#list foldersWay as folder>
                    <li class="breadcrumb-item rounded-0"><a href="/folders/${folder.name}">${folder.name}</a></li>
            </#list>
        </ol>
    </nav>
    <div class="list-group list-group-flush">
        <#list foldersChild as folder>
            <div>
                <a href="/folders/${folder.name}" class="list-group-item list-group-item-action h6"><img src="img/folder.svg"> ${folder.name}</a>
            </div>
        <#else>
            <a href="" class="list-group-item list-group-item-action h6">...Nothing</a>
        </#list>

        <#list files as file>
        <div>
            <a href="" class="list-group-item list-group-item-action h6"><img src="img/file.svg"> ${file.name}</a>
        </div>
        <#else>
        <a href="" class="list-group-item list-group-item-action h6">...Nothing</a>
        </#list>
    </div>
    <#list exceptions as exception>
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        ${exception}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    </#list>