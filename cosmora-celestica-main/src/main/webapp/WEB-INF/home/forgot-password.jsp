<%-- 
    Document   : forgot-password
    Created on : Jun 10, 2025, 9:14:11 PM
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
                            <div class="sign__form">
                                <a href="/home" class="sign__logo">
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

                                <!-- Send OTP Form -->
                                <form action="${pageContext.servletContext.contextPath}/forgot-password" method="POST" id="sendOtpForm" class="sign__group">
                                    <input type="hidden" name="action" value="sendOtp"/>
                                    <div class="sign__group sign__group--otp">
                                        <input type="email" value="${requestScope.email}" class="sign__input" placeholder="Email" name="email" id="emailForgotInput" value="${currentForgotEmail}" required>
                                        <button type="submit" id="sendOtpForgotBtn" class="send-otp-link">Send OTP</button>
                                    </div>
                                    <p id="cooldownText" style="text-align: right; font-size: 12px; color: #999;"></p>
                                </form>
                                <form action="${pageContext.servletContext.contextPath}/forgot-password" method="POST" id="verifyOtpForm" class="sign__group">
                                    <input type="hidden" name="action" value="verifyOtp"/>
                                    <input type="hidden" name="email" id="emailForgotPassword" value=""/>
                                    <div class="sign__group" id="otpSection">
                                        <input type="text" class="sign__input" name="otp" id="otpForgotInput" placeholder="Enter OTP" required>
                                        <button type="submit" id="verifyOtpForgotBtn" class="sign__btn">Verify OTP</button>
                                    </div>
                                </form>
                                <span class="sign__text">An OTP will be sent to your registered email address.</span>
                                <a type="button" href="${pageContext.servletContext.contextPath}/login" class="sign__goback">Go Back</a>
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
                // Validation for Send OTP Form
                const sendOtpForm = document.getElementById("sendOtpForm");

                if (sendOtpForm) {
                    sendOtpForm.addEventListener("submit", function (event) {
                        const email = document.getElementById('emailForgotInput').value.trim();
                        const errorMessage = isValidEmail(email);
                        if (errorMessage) {
                            event.preventDefault(); // Prevent submission
                            showMessage(errorMessage);
                            return;
                        }
                        showMessage("Processing...");
                    });
                }

                // Validation for Verify OTP Form
                const verifyOtpForm = document.getElementById("verifyOtpForm");

                if (verifyOtpForm) {
                    verifyOtpForm.addEventListener("submit", function (event) {
                        // Get OTP and Email values
                        const otp = document.getElementById('otpForgotInput').value.trim();
                        const email = document.getElementById('emailForgotInput').value.trim();

                        // Validate OTP
                        const errorMessage = isValidOtp(otp);
                        if (errorMessage) {
                            event.preventDefault(); // Prevent submission
                            showMessage(errorMessage);
                            return;
                        }

                        // Validate Email (just in case it's empty or invalid at this point)
                        const emailErrorMessage = isValidEmail(email);
                        if (emailErrorMessage) {
                            event.preventDefault(); // Prevent submission
                            showMessage(emailErrorMessage);
                            return;
                        }

                        // Set the email value to the hidden input in the verifyOtpForm before submitting
                        const hiddenEmailField = verifyOtpForm.querySelector('input[id="emailForgotPassword"]');
                        hiddenEmailField.value = email; // Set its value

                        // Show verification message
                        showMessage("Verifying OTP...");
                    });
                }


                // Email validation function
                function isValidEmail(email) {
                    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                    if (!email) {
                        return "Please enter your email.";
                    }
                    if (!emailRegex.test(email)) {
                        return "Please enter a valid email address (example@gmail.com).";
                    }
                    return null;
                }

                // OTP validation function
                function isValidOtp(otp) {
                    const otpRegex = /^[0-9]+$/;
                    if (!otp) {
                        return "Please enter your OTP.";
                    }
                    if (!otpRegex.test(otp) || otp.length !== 6) {
                        return "Please enter a valid OTP (6-digit).";
                    }
                    return null;
                }

                // Function to display messages
                function showMessage(message) {
                    const messageContainer = document.getElementById("messageText");
                    if (messageContainer) {
                        messageContainer.innerHTML = message;
                    }
                }
            </script>
