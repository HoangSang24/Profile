<%-- 
    Document   : login
    Created on : Jun 10, 2025, 4:14:46 PM
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
        <title>Cosmora Celestica</title>

    </head>

    <body>
        <!-- sign in -->
        <div class="sign section--full-bg" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="sign__content">
                            <!-- authorization form -->
                            <form action="${pageContext.servletContext.contextPath}/login" method="POST" id="loginForm" class="sign__form">
                                <a href="${pageContext.servletContext.contextPath}/home" class="sign__logo">
                                    <img src="${pageContext.servletContext.contextPath}/assets/img/logo.png" alt="">
                                </a>

                                <!-- Message Container -->
                                <div id="message" style="color: yellow; margin-bottom: 15px;">
                                    <p id="messageText">
                                        <c:if test="${not empty message}">
                                            ${message}
                                        </c:if>
                                    </p>
                                </div>
                                <%
                                    request.getSession().removeAttribute("message");
                                %>

                                <div class="sign__group">
                                    <input type="email" class="sign__input" placeholder="Email" name="email" id="email" value="${requestScope.email}" autocomplete="username" required>
                                </div>

                                <div class="sign__group">
                                    <input type="password" class="sign__input" name="password" id="password" value="${requestScope.password}" placeholder="Password"
                                           autocomplete="new-password" required>
                                    <!-- Eye Icon for toggling password visibility -->
                                    <i class="fa-solid fa-eye" id="eyeIcon" style="position: absolute; right: 5px; top: 50%; transform: translateY(-50%); font-size: 14px; color: #888; cursor: pointer; z-index: 999;"></i>
                                </div>

                                <button class="sign__btn" type="submit">Sign in</button>

                                <span class="sign__delimiter">or</span>

                                <div class="sign__social">
                                    <a class="gl" href="${pageContext.servletContext.contextPath}/login-google"><svg xmlns='http://www.w3.org/2000/svg' class='ionicon'
                                                                                                                     viewBox='0 0 512 512'>
                                        <path
                                            d='M473.16 221.48l-2.26-9.59H262.46v88.22H387c-12.93 61.4-72.93 93.72-121.94 93.72-35.66 0-73.25-15-98.13-39.11a140.08 140.08 0 01-41.8-98.88c0-37.16 16.7-74.33 41-98.78s61-38.13 97.49-38.13c41.79 0 71.74 22.19 82.94 32.31l62.69-62.36C390.86 72.72 340.34 32 261.6 32c-60.75 0-119 23.27-161.58 65.71C58 139.5 36.25 199.93 36.25 256s20.58 113.48 61.3 155.6c43.51 44.92 105.13 68.4 168.58 68.4 57.73 0 112.45-22.62 151.45-63.66 38.34-40.4 58.17-96.3 58.17-154.9 0-24.67-2.48-39.32-2.59-39.96z' />
                                        </svg></a>
                                </div>

                                <span class="sign__text">Don't have an account? <a href="${pageContext.servletContext.contextPath}/register">Register!</a></span>

                                <span class="sign__text"><a href="${pageContext.servletContext.contextPath}/forgot-password">Forgot password?</a></span>
                                <a type="button" href="${pageContext.servletContext.contextPath}/home" class="sign__goback">Go Back Home</a>
                            </form>
                            <!-- end authorization form -->
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Facebook Button -->
        <a href="https://www.facebook.com/YourPage" 
           style="position: fixed;
           bottom: 20px;
           right: 20px;
           background-color: #4267B2;
           color: white;
           padding: 15px 20px;
           border-radius: 50%;
           font-size: 24px;
           display: flex;
           align-items: center;
           justify-content: center;
           box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
           transition: background-color 0.3s, transform 0.3s;
           text-decoration: none;
           cursor: pointer;
           z-index: 100;">
            <i class="fab fa-facebook-f" style="font-size: 24px;"></i>
        </a>

        <!-- Phone Button -->
        <a href="tel:+1234567890" 
           id="phoneButton"
           style="position: fixed;
           bottom: 80px;
           right: 20px;
           background-color: #34b7f1;
           color: white;
           padding: 15px;
           border-radius: 50%;
           font-size: 24px;
           display: flex;
           align-items: center;
           justify-content: center;
           box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
           transition: background-color 0.3s, transform 0.3s;
           text-decoration: none;
           cursor: pointer;
           z-index: 100;">
            <i class="fas fa-phone-alt"></i>
            <span id="phoneText" style="display: none;
                  position: absolute;
                  top: -30px;
                  left: 0%;
                  transform: translateX(-50%);
                  background-color: #34b7f1;
                  color: white;
                  padding: 5px 10px;
                  border-radius: 5px;
                  font-size: 14px;">+1234567890</span>
        </a>

        <script>
            // Hover effect to show phone number
            document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseover', function () {
                document.getElementById('phoneText').style.display = 'block';
            });

            document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseout', function () {
                document.getElementById('phoneText').style.display = 'none';
            });

            // Click-to-copy phone number functionality
            document.getElementById('phoneButton').addEventListener('click', function (e) {
                e.preventDefault(); // Prevent the default action (making a call)

                // Create a temporary input element to copy the phone number
                const tempInput = document.createElement('input');
                tempInput.value = "+1234567890"; // Phone number to copy
                document.body.appendChild(tempInput);
                tempInput.select();
                tempInput.setSelectionRange(0, 99999); // For mobile devices

                // Copy the text to the clipboard
                document.execCommand('copy');

                // Remove the temporary input element
                document.body.removeChild(tempInput);

                // Display a message or change button style to indicate success
                alert('Phone number copied to clipboard!');
            });
        </script>
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
            const loginForm = document.getElementById("loginForm");

            if (loginForm) {
                loginForm.addEventListener("submit", function (event) {

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