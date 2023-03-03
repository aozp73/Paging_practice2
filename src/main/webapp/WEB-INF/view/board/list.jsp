<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>
        <input id="checkKeyword" type="hidden" name="" value="${pagingDto.keyword}">



        <div class="container my-3 py-3 px-3">
            <div class="p-3">
                <h2 style="text-align: center;">채용 공고 목록</h2>
            </div>
            <c:if test="${principal.role == 'employee'}">
                <div class="d-flex justify-content-end mb-2">
                    <select id="selectBox" class="form-select" style="width: 123px;"
                        onchange="if(this.value) location.href=(this.value);">
                        <option value="#">정렬</option>
                        <option value="/board/list?keyword=lang">관련도순</option>
                        <option value="#"> 인기순</option>
                        <option value="#"> 날짜순</option>
                    </select>
                </div>
            </c:if>
            <!-- 카드 시작 -->
            <div class="p-3">
                <div class="row gx-3">

                    <c:forEach items="${pagingDto.boardListDtos}" var="board">
                        <div class="col-md-3 py-2">
                            <a href="/board/${board.id}" class="no_under_line_link">
                                <div id="boardImage-${board.id}" class="card col-lg-12"
                                    onmouseenter="mouseEnterImages(this)" onmouseleave="mouseLeaveImages(this)">
                                    <div class="d-flex justify-content-center">
                                        <img class="card-img-top" style="height: 100px; max-width: 200px;"
                                            src="${board.profile == null ? " /images/defaultProfile.png" :
                                            board.profile}" alt="Card image">
                                    </div>
                                    <div class="card-body">
                                        <div class="my-text-ellipsis">
                                            <h5>${board.companyName}</h5>
                                        </div>
                                        <div class="my-text-ellipsis">
                                            ${board.title}
                                        </div>
                                        <div class="my-text-ellipsis">
                                            채용
                                        </div>
                                    </div>
                            </a>
                            <div class="card-footer d-flex justify-content-between">
                                <div>(D-1)</div>
                                <div><i id="heart" class="fa-regular fa-heart my-xl my-cursor fa-lg"></i></div>
                            </div>
                        </div>
                </div>
                </c:forEach>

            </div>
        </div>

        <!-- 페이징 -->

            <div class="d-flex justify-content-center">
                <ul class="pagination">

                    <li class='page-item ${pagingDto.first ? "disabled" : ""}'><a class="page-link"
                            href="javascript:void(0);" onclick="callPrev();">Prev</a></li>

                    <c:forEach var="num" begin="${pagingDto.startPageNum}" end="${pagingDto.lastPageNum}">
  
                            <li class='page-item'><a class='page-link' href="/board/list?page=${num-1}&keyword=${pagingDto.keyword}">${num}</a></li>
             
                            <%-- <li class='page-item'><a class='page-link' href="/board/list?page=${num-1}">${num}</a></li> --%>
                  </c:forEach>

                    <li class='page-item ${pagingDto.last ? "disabled" : ""}'><a class="page-link"
                            href="javascript:void(0);" onclick="callNext();">Next</a></li>

                </ul>
            </div>
        </div>

        <script>
            // alert(${pagingDto.keyword});
            // alert()

            function callPrev() {
                let keyword = $("#checkKeyword").val();
                // let keyword = $("#checkKeyword").val();
                // alert(keyword)
                let currentPage = `${pagingDto.currentPage - 1}`
                // alert(currentPage)
                if (keyword) {
                    location.href = "/board/list?page=" + currentPage + "&keyword=" + keyword;
                } else {
                    location.href = "/board/list?page=" + currentPage;
                }
            }

            function callNext() {
                
                let keyword = $("#checkKeyword").val();
                // alert(keyword)
                let currentPage = `${pagingDto.currentPage + 1}`
                    //    alert(currentPage)
                if (keyword) {
                    location.href = "/board/list?page=" + currentPage + "&keyword=" + keyword;
                } else {
                    location.href = "/board/list?page=" + currentPage;
                }
            }



            function selectBoxCheck() {
                let check = $("#checkKeyword").val()
                // alert(check)
                if (check == "lang") {
                    $("#selectBox").val("/board/list?keyword=lang").prop("selected",true);
                }
            }

            selectBoxCheck()

            function selectBoxChange(value) {
                console.log(value);
            }

            function mouseEnterImages(e) {
                // console.log(e.getAttribute('id'))
                let id = e.getAttribute('id');
                $("#" + id).addClass("border border-primary");
            }
            function mouseLeaveImages(e) {
                let id = e.getAttribute('id');
                $("#" + id).removeClass("border border-primary");
            }
        </script>

        <%@ include file="../layout/footer.jsp" %>