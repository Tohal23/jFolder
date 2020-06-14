<#import "part/common.ftlh" as c>

<@c.page>
    <div class="content h-100">
        <div class="row no-gutters h-100">
            <#include "part/security.ftlh">
            <div class="modal d-inline bg-light" tabindex="-1" role="dialog" aria-labelledby="myExtraLargeModalLabel">
                <div class="modal-dialog modal-xl" style="width:90%">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">(${excpetionName})</h5>
                            <a href="javascript:history.back()">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </a>
                        </div>
                        <div class="modal-body">
                            <p>
                                ${description}
                            </p>
                        </div>
                        <div class="modal-footer">
                            <a href="javascript:history.back()"><button type="button" class="btn btn-primary">Close</button></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>