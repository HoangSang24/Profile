<%-- 
    Document   : login-dashboard
    Created on : Jun 16, 2025, 7:18:07 PM
    Author     : CE190449 - Le Anh Khoa
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-reboot.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/bootstrap-grid.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/owl.carousel.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/magnific-popup.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/nouislider.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/jquery.mCustomScrollbar.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/paymentfont.min.css">
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/assets/css/main.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

        <!-- Favicons -->
        <link rel="icon" type="image/png" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png" sizes="32x32">
        <link rel="apple-touch-icon" href="${pageContext.servletContext.contextPath}/assets/icon/logo.png">

        <meta name="description" content="Cosmora Celestica - Selling games and gaming accessories website">
        <meta name="keywords" content="">
        <title>Cosmora Celestica – Games and Accessories</title>

    </head>

    <body>
        <!-- sign in -->
        <div class="sign section--full-bg" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="sign__content">
                            <!-- authorization form -->
                            <form action="${pageContext.servletContext.contextPath}/login-dashboard" method="POST" id="loginDashboardForm" class="sign__form">
                                <a href="${pageContext.servletContext.contextPath}/home" class="sign__logo">
                                    <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                </a>
                                <h2 style="    font-size: 1.8rem;
                                    font-weight: bold;
                                    color: #00B0FF;
                                    text-align: center;
                                    margin-bottom: 20px;
                                    text-transform: uppercase;
                                    letter-spacing: 1px;">
                                    Dashboard
                                </h2>

                                <!-- Message Container -->
                                <div id="message" style="color: yellow; margin-bottom: 15px;">
                                    <p id="messageText">
                                        <c:if test="${not empty message}">
                                            ${message}
                                        </c:if>
                                    </p>
                                </div>

                                <div class="sign__group">
                                    <input type="email" class="sign__input" placeholder="Email" name="email" id="email" value="${requestScope.email}" autocomplete="username">
                                </div>

                                <div class="sign__group">
                                    <input type="password" class="sign__input" name="password" id="password" value="${requestScope.password}" placeholder="Password"
                                           autocomplete="new-password">
                                    <!-- Eye Icon for toggling password visibility -->
                                    <i class="fa-solid fa-eye" id="eyeIcon" style="position: absolute; right: 5px; top: 50%; transform: translateY(-50%); font-size: 14px; color: #888; cursor: pointer; z-index: 999;"></i>
                                </div>

                                <button class="sign__btn" type="submit">Sign in</button>

                                <a type="button" href="${pageContext.servletContext.contextPath}/home" class="sign__goback">Go Back</a>
                            </form>
                            <!-- end authorization form -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- end sign in -->
        <!-- JS -->
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery-3.5.1.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/owl.carousel.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.magnific-popup.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/wNumb.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/nouislider.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.mousewheel.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/jquery.mCustomScrollbar.min.js"></script>
        <script src="${pageContext.servletContext.contextPath}/assets/js/main.js"></script>
        <script>
            const loginDashboardForm = document.getElementById("loginDashboardForm");

            if (loginDashboardForm) {
                loginDashboardForm.addEventListener("submit", function (event) {

                    const email = document.getElementById('email').value.trim();
                    const password = document.getElementById('password').value.trim();

                    showMessage("Processing...");

                    const errorMessage = isValidLogin(email, password);
                    if (errorMessage) {
                        event.preventDefault(); // Prevent submission
                        showMessage(errorMessage);
                        return;
                    }
                    // Form submits normally after this
                });
            }

            function isValidLogin(email, password) {
                const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|googlemail\.com)$/;
                if (!emailRegex.test(email)) {
                    return "Please enter a valid Google email address (gmail.com or googlemail.com).";
                }

                const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
                if (password.length < 8) {
                    return "Password must be at least 8 characters long.";
                } else if (!passwordRegex.test(password)) {
                    return "Password must contain at least 1 letter and 1 number.";
                }

                return null;
            }

            function showMessage(msg) {
                document.getElementById('messageText').textContent = "";
                document.getElementById('messageText').textContent = msg;
            }
        </script>
        <script>
            const eyeIcon = document.getElementById('eyeIcon');
            const passwordInput = document.getElementById('password');

            eyeIcon.addEventListener('click', function () {
                // Toggle the type attribute between 'password' and 'text'
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';  // Show password
                    eyeIcon.classList.remove('fa-eye');  // Remove eye icon
                    eyeIcon.classList.add('fa-eye-slash');  // Show eye-slash icon (indicates password is visible)
                } else {
                    passwordInput.type = 'password';  // Hide password
                    eyeIcon.classList.remove('fa-eye-slash');  // Remove eye-slash icon
                    eyeIcon.classList.add('fa-eye');  // Show eye icon (indicates password is hidden)
                }
            });
        </script>
    </body>

</html>
