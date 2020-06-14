    <#include "security.ftlh">
    <div class="modal d-inline bg-light" tabindex="-1" role="dialog" aria-labelledby="myExtraLargeModalLabel">
        <div class="modal-dialog modal-xl" style="width:90%">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Versions of file (${fileName})</h5>
                    <a href="/folders/${folderName}">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </a>
                </div>
                <div class="modal-body">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Date</th>
                            <th scope="col">Description</th>
                            <th scope="col">Last</th>
                            <th scope="col"> </th>
                            <th scope="col"> </th>
                            <th scope="col"> </th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list versions as version>
                            <tr>
                                <th scope="row">${version.number}</th>
                                <td>${version.date}</td>

                                <form action="/file/versions/update/${folderName}/${fileName}" method="post">
                                    <td>
                                        <input type="text" name="description" class="form-control"
                                               value=<#if version.description??>${version.description}</#if>
                                        >

                                    </td>
                                    <td>
                                        <input type="checkbox" name="last" class="align-middle"
                                               style="width: 20px; height: 20px"
                                               ${version.last?then('checked=Y', '')} value="true"
                                        />
                                    </td>
                                    <td>
                                        <button type="submit" class="btn btn-primary">Change</button>
                                    </td>
                                    <input type="hidden" name="id" value="${version.id}" />
                                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                </form>
                                <td>
                                    <form action="/file/versions/delete/${folderName}/${fileName}" method="post">
                                        <button class="btn btn-primary">Delete</button>
                                        <input type="hidden" name="id" value="${version.id}" />
                                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                    </form>
                                </td>
                                <td>
                                    <a href="/file/get-file/${folderName}/${fileName}/${version.number}">
                                        <button class="btn btn-primary">Download</button>
                                    </a>

                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <form action="/file/versions/add/${folderName}/${fileName}" method="post" enctype="multipart/form-data">
                        <div class="input-group">
                            <div class="input-group-prepend">
                                <button class="btn btn-outline-secondary" type="submit" id="inputGroupFileAddon03">Add new version</button>
                            </div>
                            <div class="custom-file">
                                <input type="file" class="custom-file-input" id="inputGroupFile03" aria-describedby="inputGroupFileAddon03" name="file">
                                <label class="custom-file-label" for="inputGroupFile03">Choose file</label>
                            </div>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    </form>
                </div>
            </div>
        </div>
    </div>
