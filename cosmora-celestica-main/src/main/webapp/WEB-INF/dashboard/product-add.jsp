<%--
    Document : product-add
    Created on : Jun 10, 2025, 10:55:00 PM
    Author     : HoangSang
--%>

<%@page import="shop.model.OperatingSystem"%>
<%@page import="shop.model.StorePlatform"%>
<%@page import="shop.model.Category"%>
<%@page import="shop.model.Brand"%>
<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<%
    List<Category> categoriesList = (List<Category>) request.getAttribute("categoriesList");
    List<Brand> brandsList = (List<Brand>) request.getAttribute("brandsList");
    List<StorePlatform> allPlatforms = (List<StorePlatform>) request.getAttribute("allPlatforms");
    List<OperatingSystem> allOS = (List<OperatingSystem>) request.getAttribute("allOS");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<style>
    /* CSS không thay đổi, giữ nguyên */
    .image-uploader {
        border: 2px dashed #ddd;
        border-radius: 8px;
        padding: 20px;
        text-align: center;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 100%;
        height: 180px;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #4a4e69;
        transition: border-color 0.3s;
    }
    .image-uploader:hover {
        border-color: #007bff;
    }
    .image-uploader__content {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }
    .image-uploader__icon {
        font-size: 40px;
        color: #aaa;
    }
    .image-uploader p {
        margin: 10px 0 0;
        color: #aaa;
    }
    .image-uploader input[type="file"] {
        display: none;
    }
    .image-preview {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: none;
    }
    .remove-image-btn {
        position: absolute;
        top: 5px;
        right: 5px;
        background-color: rgba(255, 0, 0, 0.7);
        color: white;
        border: none;
        border-radius: 50%;
        width: 25px;
        height: 25px;
        font-size: 16px;
        line-height: 25px;
        text-align: center;
        cursor: pointer;
        display: none;
        z-index: 10;
    }
    .admin-manage-subtitle {
        font-size: 1.25rem;
        font-weight: 500;
        margin-bottom: 1rem;
        padding-bottom: 0.5rem;
    }
    .add-1{
        background-color: #00b7ff;
        padding: 5px 10px 5px 10px;
        border-radius: 7px;
        color: #ffffff;
        font-weight: 600;
    }

    .remove-attr-btn {
        width: 100%;
        padding: 5px;
        border-radius: 7px;
    }
</style>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Create New Product</h2>
    </div>

    <div class="admin-manage-wrapper container py-4">
        <div class="mb-4">
            <a href="<%= request.getContextPath()%>/manage-products?action=list" class="admin-manage-back mb-5">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
        </div>

        <form action="<%= request.getContextPath()%>/manage-products?action=add" method="post" enctype="multipart/form-data">
            <input type="hidden" id="productType" name="productType" value="">

            <%-- Hiển thị thông báo thành công/lỗi --%>
            <% if (successMessage != null && !successMessage.isEmpty()) {%>
            <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;"><%= successMessage%></div>
            <% } %>
            <% if (errorMessage != null && !errorMessage.isEmpty()) {%>
            <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;"><%= errorMessage%></div>
            <% } %>

            <div class="mb-4">
                <label for="categoryId" class="form-label admin-manage-label">Product Type</label>
                <select class="custom-select admin-manage-input" id="categoryId" name="categoryId" required onchange="handleProductTypeChange(this)">
                    <option value="">-- Choose a Product Type --</option>
                    <% if (categoriesList != null) {
                            for (Category cat : categoriesList) {
                                String normalizedName = cat.getName().toLowerCase().replaceAll("\\s+", "")
                                        .replace("chuột", "mouse")
                                        .replace("bànphím", "keyboard")
                                        .replace("tai nghe", "headphone") // Thay đổi "tainghe" -> "tai nghe" để khớp hơn
                                        .replace("tay cầm (controller)", "controller") // Thay đổi "taycầm(controller)" -> "tay cầm (controller)"
                                        .replace("game", "game");
                    %>
                    <option value="<%= cat.getCategoryId()%>" data-normalized-name="<%= normalizedName%>"><%= cat.getName()%></option>
                    <% }
                        } %>
                </select>
            </div>

            <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                <h3 class="admin-manage-subtitle">General Information</h3>
                <div class="row g-3">
                    <div class="col-md-12">
                        <label class="form-label admin-manage-label">Product Name</label>
                        <input type="text" class="form-control admin-manage-input" name="name" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Price ($)</label>
                        <input type="number" step="0.01" min="0.01" class="form-control admin-manage-input" name="price" required>
                    </div>
                    <div class="col-md-6" id="quantity-field-wrapper">
                        <label class="form-label admin-manage-label">Quantity</label>
                        <input type="number" min="1" class="form-control admin-manage-input" id="quantity-input" name="quantity" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input" name="description" rows="3" required></textarea>
                    </div>
                </div>
            </div>

            <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                <h3 class="admin-manage-subtitle">Product Images (At least 1, up to 6 images)</h3>
                <div class="row g-3" id="image-upload-area">
                    <% for (int i = 1; i <= 6; i++) {%>
                    <div class="col-md-4 col-sm-6 mb-3">
                        <label class="image-uploader" for="productImage<%= i%>">
                            <div class="image-uploader__content">
                                <i class="fas fa-cloud-upload-alt image-uploader__icon"></i>
                                <p>Click to upload Image <%=i%></p>
                            </div>
                            <img src="" alt="Preview <%= i%>" class="image-preview">
                            <input type="file" id="productImage<%= i%>" name="productImages" accept="image/*" <% if (i == 1) { %> required <% } %>>
                            <button type="button" class="remove-image-btn">&times;</button>
                        </label>
                    </div>
                    <% } %>
                </div>
            </div>

            <div id="dynamicFieldsContainer">

                <%-- Game Section --%>
                <div class="admin-manage-type game-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Game Details</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Developer</label>
                                <input type="text" name="developer" class="form-control admin-manage-input">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Genre</label>
                                <input type="text" name="genre" class="form-control admin-manage-input" placeholder="e.g., Action, RPG">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Release Date</label>
                                <input type="date" name="releaseDate" class="form-control admin-manage-input">
                            </div>
                        </div>
                    </div>
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Game Activation & System</h3>
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label admin-manage-label">Store Platform</label>
                                <select multiple class="custom-select admin-manage-input" name="platformIds" size="4">
                                    <% if (allPlatforms != null) {
                                            for (StorePlatform p : allPlatforms) {%>
                                    <option value="<%= p.getPlatformId()%>"><%= p.getStoreOSName()%></option><% }
                                            } %>
                                </select>
                            </div>
                            <div class="col-12">
                                <label class="form-label admin-manage-label">Supported OS</label>
                                <select multiple class="custom-select admin-manage-input" name="osIds" size="4">
                                    <% if (allOS != null) {
                                            for (OperatingSystem os : allOS) {%>
                                    <option value="<%= os.getOsId()%>"><%= os.getOsName()%></option><% }
                                            } %>
                                </select>
                            </div>
                            <div class="col-12">
                                <label for="gameKeys" class="form-label admin-manage-label">Game Keys (one key per line)</label>
                                <textarea id="gameKeys" name="gameKeys" class="form-control admin-manage-input" rows="8"></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Common Accessory Details Section --%>
                <div class="admin-manage-type accessory-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Accessory Details</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Warranty (months)</label>
                                <input type="number" name="warrantyMonths" class="form-control admin-manage-input" min="0">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Weight (grams)</label>
                                <input type="number" step="0.01" name="weightGrams" class="form-control admin-manage-input" min="0.01">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Connection Type</label>
                                <input type="text" name="connectionType" class="form-control admin-manage-input" placeholder="e.g., Wireless, USB-C">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Usage Time (hours)</label>
                                <input type="number" step="0.1" name="usageTimeHours" class="form-control admin-manage-input" min="0">
                            </div>
                            <div class="col-md-12">
                                <label class="form-label admin-manage-label">Brand</label>
                                <select class="custom-select admin-manage-input" name="brandId">
                                    <option value="">-- Select Brand --</option>
                                    <% if (brandsList != null) {
                                            for (Brand brand : brandsList) {%>
                                    <option value="<%= brand.getBrandId()%>"><%= brand.getBrandName()%></option><% }
                                            }%>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Headphone Specifics --%>
                <div class="admin-manage-type headphone-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Headphone Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Headphone Type</label>
                                <input type="text" name="headphoneType" class="form-control admin-manage-input" placeholder="e.g., Over-ear, In-ear">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" name="headphoneMaterial" class="form-control admin-manage-input">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Battery Capacity (mAh)</label>
                                <input type="number" name="headphoneBattery" class="form-control admin-manage-input" min="0">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label admin-manage-label">Features</label>
                                <textarea name="headphoneFeatures" class="form-control admin-manage-input" placeholder="e.g., Noise Cancelling"></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Keyboard Specifics --%>
                <div class="admin-manage-type keyboard-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Keyboard Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Size</label>
                                <input type="text" name="keyboardSize" class="form-control admin-manage-input" placeholder="e.g., Full-size, TKL">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" name="keyboardMaterial" class="form-control admin-manage-input">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Keyboard Type</label>
                                <input type="text" name="keyboardType" class="form-control admin-manage-input" placeholder="e.g., Mechanical, Membrane">
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Mouse Specifics --%>
                <div class="admin-manage-type mouse-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Mouse Specifics</h3>
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label admin-manage-label">Mouse Type</label>
                                <input type="text" name="mouseType" class="form-control admin-manage-input" placeholder="e.g., Gaming, Ergonomic">
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Controller Specifics --%>
                <div class="admin-manage-type controller-details" style="display: none;">
                    <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                        <h3 class="admin-manage-subtitle">Controller Specifics</h3>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Material</label>
                                <input type="text" name="controllerMaterial" class="form-control admin-manage-input">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Battery Capacity (mAh)</label>
                                <input type="number" name="controllerBattery" class="form-control admin-manage-input" min="0">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label admin-manage-label">Charging Time (hours)</label>
                                <input type="number" step="0.1" name="controllerChargingTime" class="form-control admin-manage-input" min="0">
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="mb-4 admin-manage-fieldset p-4 border rounded">
                <h3 class="admin-manage-subtitle">Custom Attributes</h3>
                <button type="button" id="addCustomAttributeBtn" class="btn btn-secondary mb-3 add-1">
                    + Add Custom Attribute
                </button>
                
                <div id="customAttributesContainer"></div>
            </div>


            <div class="d-flex justify-content-end align-items:center mt-4">
                <button type="reset" class="btn admin-manage-reset mr-2"><i class="fas fa-xmark mr-1"></i> Reset</button>
                <button type="submit" class="btn admin-manage-button"><i class="fas fa-plus mr-1"></i> Create</button>
            </div>
        </form>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Javascript cho image-uploader không thay đổi
        document.querySelectorAll('.image-uploader').forEach(uploader => {
            const input = uploader.querySelector('input[type="file"]');
            const preview = uploader.querySelector('.image-preview');
            const content = uploader.querySelector('.image-uploader__content');
            const removeBtn = uploader.querySelector('.remove-image-btn');
            input.addEventListener('change', function () {
                const file = this.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        preview.src = e.target.result;
                        preview.style.display = 'block';
                        removeBtn.style.display = 'block';
                        content.style.display = 'none';
                    };
                    reader.readAsDataURL(file);
                }
            });
            removeBtn.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                input.value = '';
                preview.src = '';
                preview.style.display = 'none';
                removeBtn.style.display = 'none';
                content.style.display = 'flex';
            });
        });

        // Cập nhật trạng thái form khi trang được tải lần đầu
        handleProductTypeChange(document.getElementById('categoryId'));
    });

    /**
     * Hàm này kết hợp cả 2 chức năng:
     * 1. Xử lý logic cho trường Số lượng (Quantity).
     * 2. Hiện/ẩn và bật/tắt các trường động để `required` hoạt động đúng.
     */
    function handleProductTypeChange(selectElement) {
        const selectedOption = selectElement.options[selectElement.selectedIndex];
        const productType = selectedOption.getAttribute('data-normalized-name');
        document.getElementById('productType').value = productType; // Cập nhật hidden input

        const quantityInput = document.getElementById('quantity-input');
        const brandSelect = document.querySelector('select[name="brandId"]');
        const gameRequiredInputs = document.querySelectorAll('.game-details input[required], .game-details select[required], .game-details textarea[required]');
        const accessoryRequiredInputs = document.querySelectorAll('.accessory-details input[required], .accessory-details select[required], .accessory-details textarea[required]');
        const headphoneRequiredInputs = document.querySelectorAll('.headphone-details input[required], .headphone-details select[required], .headphone-details textarea[required]');
        const keyboardRequiredInputs = document.querySelectorAll('.keyboard-details input[required], .keyboard-details select[required], .keyboard-details textarea[required]');
        const mouseRequiredInputs = document.querySelectorAll('.mouse-details input[required], .mouse-details select[required], .mouse-details textarea[required]');
        const controllerRequiredInputs = document.querySelectorAll('.controller-details input[required], .controller-details select[required], .controller-details textarea[required]');

        // BƯỚC 1: XỬ LÝ TRƯỜNG SỐ LƯỢNG (QUANTITY) & Brand
        if (productType === 'game') {
            quantityInput.value = 1;
            quantityInput.readOnly = true;
            quantityInput.removeAttribute('required'); // Loại bỏ required cho game
            if (brandSelect)
                brandSelect.removeAttribute('required'); // Game không cần brand
        } else {
            // Đối với phụ kiện, số lượng là bắt buộc và có thể nhập
            quantityInput.value = '';
            quantityInput.readOnly = false;
            quantityInput.setAttribute('required', 'required');
            if (brandSelect)
                brandSelect.setAttribute('required', 'required'); // Phụ kiện cần brand
        }


        // BƯỚC 2: VÔ HIỆU HÓA, ẩn và loại bỏ `required` khỏi tất cả các trường động
        document.querySelectorAll('#dynamicFieldsContainer .admin-manage-type').forEach(wrapper => {
            wrapper.style.display = 'none';
            wrapper.querySelectorAll('input, select, textarea').forEach(input => {
                input.disabled = true;
                input.removeAttribute('required');
            });
        });

        // Nếu chưa chọn loại sản phẩm thì dừng lại
        if (!productType)
            return;

        // BƯỚC 3: KÍCH HOẠT, hiện lại và thêm `required` cho các trường cần thiết
        if (productType === 'game') {
            const gameDetailsBlock = document.querySelector('.game-details');
            if (gameDetailsBlock) {
                gameDetailsBlock.style.display = 'block';
                gameDetailsBlock.querySelectorAll('input, select, textarea').forEach(input => {
                    input.disabled = false;
                    // Đặt lại `required` cho các trường cần thiết của game
                    if (input.name === 'developer' || input.name === 'genre' || input.name === 'releaseDate' || input.name === 'platformIds' || input.name === 'osIds' || input.name === 'gameKeys') {
                        input.setAttribute('required', 'required');
                    }
                });
            }
        } else { // Xử lý phụ kiện
            const accessoryDetailsBlock = document.querySelector('.accessory-details');
            if (accessoryDetailsBlock) {
                accessoryDetailsBlock.style.display = 'block';
                accessoryDetailsBlock.querySelectorAll('input, select, textarea').forEach(input => {
                    input.disabled = false;
                    // Đặt lại `required` cho các trường chung của phụ kiện
                    if (input.name === 'warrantyMonths' || input.name === 'weightGrams' || input.name === 'connectionType' || input.name === 'usageTimeHours' || input.name === 'brandId') {
                        input.setAttribute('required', 'required');
                    }
                });
            }

            const specificAccessoryBlock = document.querySelector('.' + productType + '-details');
            if (specificAccessoryBlock) {
                specificAccessoryBlock.style.display = 'block';
                specificAccessoryBlock.querySelectorAll('input, select, textarea').forEach(input => {
                    input.disabled = false;
                    // Đặt lại `required` cho các trường cụ thể của từng loại phụ kiện
                    // Ví dụ cho Headphone, Keyboard, Mouse, Controller
                    if (productType === 'headphone') {
                        if (input.name === 'headphoneType' || input.name === 'headphoneMaterial' || input.name === 'headphoneBattery' || input.name === 'headphoneFeatures') {
                            input.setAttribute('required', 'required');
                        }
                    } else if (productType === 'keyboard') {
                        if (input.name === 'keyboardSize' || input.name === 'keyboardMaterial' || input.name === 'keyboardType') {
                            input.setAttribute('required', 'required');
                        }
                    } else if (productType === 'mouse') {
                        if (input.name === 'mouseType') {
                            input.setAttribute('required', 'required');
                        }
                    } else if (productType === 'controller') {
                        if (input.name === 'controllerMaterial' || input.name === 'controllerBattery' || input.name === 'controllerChargingTime') {
                            input.setAttribute('required', 'required');
                        }
                    }
                });
            }
        }

        // Kiểm tra xem input file đầu tiên có required không
        const firstImageInput = document.getElementById('productImage1');
        if (firstImageInput) {
            firstImageInput.setAttribute('required', 'required');
        }
    }


    document.addEventListener('DOMContentLoaded', function () {
        const addBtn = document.getElementById('addCustomAttributeBtn');
        const container = document.getElementById('customAttributesContainer');

        addBtn.addEventListener('click', function () {
            const wrapper = document.createElement('div');
            wrapper.classList.add('row', 'g-2', 'mb-2');

            wrapper.innerHTML = `
                <div class="col-md-5">
                    <input type="text" name="customAttributeNames" class="form-control admin-manage-input" placeholder="Attribute Name" required>
                </div>
                <div class="col-md-5">
                    <input type="text" name="customAttributeValues" class="form-control admin-manage-input" placeholder="Attribute Value" required>
                </div>
                <div class="col-md-2">
                    <button type="button" class="btn btn-danger remove-attr-btn btn-delete">Delete</button>
                </div>
            `;

            container.appendChild(wrapper);

            // Xử lý nút remove
            wrapper.querySelector('.remove-attr-btn').addEventListener('click', function () {
                container.removeChild(wrapper);
            });
        });
    });
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>