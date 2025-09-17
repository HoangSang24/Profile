<%@page import="java.util.List"%>
<%@page import="shop.model.Staff"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Edit Staff</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">


        <!-- General Information -->
        <form action="${pageContext.servletContext.contextPath}/manage-staffs" method="POST" id="editStaffForm" enctype="multipart/form-data">
            <input type="hidden" name="action" value="edit" />

            <fieldset class="mb-4 admin-manage-fieldset">
                <legend class="admin-manage-subtitle">Staff Information</legend>
                <%
                    String error = (String) session.getAttribute("errorMessage");
                    if (error != null) {
                %>
                <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
                    <strong>Error:</strong> <%= error%>

                </div>
                <%} %>
                <%
                    Staff s = (Staff) request.getAttribute("s");
                    if (s == null) {
                %>
                <tr>
                    <td colspan="8" class="text-center text-danger">No Staff Found</td>
                </tr>
                <%
                } else {
                %>
                <input type="hidden" name="id" value="<%=s.getId()%>" />

                <div class="row g-3">
                    <!-- Full Name -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Full Name</label>
                        <input type="text" class="form-control admin-manage-input" value="<%=s.getFullName()%>" name="username" id="username" required>
                    </div>

                    <!-- Email -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Email</label>
                        <input type="email" class="form-control admin-manage-input" value="<%=s.getEmail()%>" name="email" id="email" readonly>
                    </div>

                    <!-- Password -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Password</label>
                        <input type="password" required class="form-control admin-manage-input" name="password"id="password">
                    </div>

                    <!-- Phone -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Phone</label>
                        <input type="phone" required class="form-control admin-manage-input" value="<%=s.getPhone()%>" name="phone" id="phone">
                    </div>

                    <!-- Gender -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Gender</label>
                        <select class="form-select admin-manage-input" name="gender" required>
                            <option value="">-- Select Gender --</option>
                            <option value="Male" <%= "Male".equalsIgnoreCase(s.getGender()) ? "selected" : ""%>>Male</option>
                            <option value="Female" <%= "Female".equalsIgnoreCase(s.getGender()) ? "selected" : ""%>>Female</option>

                        </select>
                    </div>

                    <!-- Role -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Role</label>
                        <input type="text" class="form-control admin-manage-input" name="role" value="staff" readonly required>
                    </div>

                    <!-- Avatar -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Avatar Image</label>
                        <img src="<%= s.getAvatarUrl()%>" alt="Avatar" style="width: 150px; height: 140px; border: 1px solid #ccc; margin-bottom: 10px;">
                        <input type="file" class="form-control admin-manage-input" name="avatar_url" accept=".png">
                    </div>

                    <!-- Date of Birth -->
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Date of Birth</label>
                        <%
                            String dobFormatted = "";
                            if (s.getDateOfBirth() != null) {
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                                dobFormatted = sdf.format(s.getDateOfBirth());
                            }
                        %>

                      <input type="date" id="dateOfBirth" class="form-control admin-manage-input" name="date_of_birth" value="<%= dobFormatted %>" required>
                        <small id="dobError" class="text-danger"></small>
                    </div>
                </div>
                <% }%>
            </fieldset>

            <div class="d-flex justify-content-between align-items-center mt-4">
                <a href="${pageContext.servletContext.contextPath}/manage-staffs?view=list" class="admin-manage-back">
                    <i class="fas fa-arrow-left mr-1"></i> Back
                </a>

                <div>
                    <button type="submit" class="btn admin-manage-button">
                        <i class="fas fa-plus mr-1"></i> Edit
                    </button>
                </div>
            </div>
        </form>

    </div>
</main>
<script>
// Validate form on submit
    document.getElementById("editStaffForm").addEventListener("submit", function (e) {
        const username = this.querySelector('[name="username"]').value;
        const email = this.querySelector('[name="email"]').value;
        const password = this.querySelector('[name="password"]').value.trim();
        const phone = this.querySelector('[name="phone"]').value;

        // Password is not rerquired
        if (password !== "") {
            // Password - must be at least 8 characters long and contain at least 1 letter and 1 number
            const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;

            if (password.length < 8) {
                alert("Password must be at least 8 characters long.");
                e.preventDefault();
                return;
            } else if (!passwordRegex.test(password)) {
                alert("Password must contain at least 1 letter and 1 number.");
                e.preventDefault();
                return;
            }
        }

        // Email - must be a valid Gmail address
        const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail\.com|googlemail\.com)$/;
        if (!emailRegex.test(email)) {
            alert("Please enter a valid Google email address (gmail.com or googlemail.com).");
            e.preventDefault();
            return;
        }

        // Phone - optional but must be digits and 9–15 chars if provided
        const phoneRegex = /^[0-9]{9,15}$/;
        if (phone !== '' && !phoneRegex.test(phone)) {
            alert('Phone number must contain 9 to 15 digits only.');
            e.preventDefault();
            return;
        }
    })

    document.getElementById('dateOfBirth').addEventListener('change', function () {
        const dobInput = this.value;
        const errorDisplay = document.getElementById('dobError');

        if (dobInput) {
            const dob = new Date(dobInput);
            const today = new Date();

            let age = today.getFullYear() - dob.getFullYear();
            const monthDiff = today.getMonth() - dob.getMonth();
            const dayDiff = today.getDate() - dob.getDate();

            if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
                age--;
            }

            if (age < 18) {
                errorDisplay.textContent = "You must be at least 18 years old.";
                this.value = ""; // clear invalid date
            } else {
                errorDisplay.textContent = "";
            }
        } else {
            errorDisplay.textContent = "";
        }
    });
    ;
</script>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>