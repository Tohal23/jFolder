
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