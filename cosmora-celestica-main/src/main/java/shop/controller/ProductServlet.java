package shop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import shop.dao.BrandDAO;
import shop.dao.CategoryDAO;
import shop.dao.GameKeyDAO;
import shop.dao.OperatingSystemDAO;
import shop.dao.ProductDAO;
import shop.dao.StorePlatformDAO;
import shop.model.GameDetails;
import shop.model.OperatingSystem;
import shop.model.Product;
import shop.model.ProductAttribute;
import shop.model.StorePlatform;
import jakarta.servlet.http.HttpSession; // Import HttpSession

/**
 *
 * @author HoangSang
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
@WebServlet(name = "ProductServlet", urlPatterns = {"/manage-products"})
public class ProductServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "assets/img";
    private static final int PRODUCTS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Default action is to display the list
        }
        try {
            switch (action) {
                case "add": {
                    request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                    request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                    request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                    request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-add.jsp").forward(request, response);
                    break;
                }
                case "update": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product existingProduct = productDAO.getProductById(id);

                    if (existingProduct == null) {
                        response.sendRedirect("manage-products?action=list");
                        return;
                    }

                    request.setAttribute("product", existingProduct);
                    request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                    request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                    request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                    request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());

                    if (existingProduct.getGameDetailsId() != null && existingProduct.getGameDetailsId() > 0) {
                        int gameDetailsId = existingProduct.getGameDetailsId();
                        request.setAttribute("gameDetails", existingProduct.getGameDetails());
                        request.setAttribute("gameKeys", new GameKeyDAO().findByGameDetailsId(gameDetailsId));

                        List<StorePlatform> selectedPlatforms = new StorePlatformDAO().findByGameDetailsId(gameDetailsId);
                        Set<Integer> selectedPlatformIds = new HashSet<>();
                        for (StorePlatform p : selectedPlatforms) {
                            int masterId = new StorePlatformDAO().getMasterStorePlatformIdByName(p.getStoreOSName());
                            if (masterId != -1) {
                                selectedPlatformIds.add(masterId);
                            }
                        }
                        request.setAttribute("selectedPlatformIds", selectedPlatformIds);

                        List<OperatingSystem> selectedOS = new OperatingSystemDAO().findByGameDetailsId(gameDetailsId);
                        Set<Integer> selectedOsIds = new HashSet<>();
                        for (OperatingSystem os : selectedOS) {
                            int masterId = new OperatingSystemDAO().getMasterOsIdByName(os.getOsName());
                            if (masterId != -1) {
                                selectedOsIds.add(masterId);
                            }
                        }
                        request.setAttribute("selectedOsIds", selectedOsIds);
                    }

                    Map<String, String> attributeMap = new HashMap<>();
                    if (existingProduct.getAttributes() != null) {
                        for (ProductAttribute attr : existingProduct.getAttributes()) {
                            attributeMap.put(attr.getAttributeName(), attr.getValue());
                        }
                    }
                    request.setAttribute("attributeMap", attributeMap);

                    request.getRequestDispatcher("/WEB-INF/dashboard/product-edit.jsp").forward(request, response);
                    break;
                }
                case "details": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product product = productDAO.getProductById(id);

                    if (product == null) {
                        response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                        return;
                    }

                    double stars = productDAO.getAverageStarsForProduct(product.getProductId());
                    product.setAverageStars(stars);

                    if ("Game".equalsIgnoreCase(product.getCategoryName()) && product.getGameDetailsId() != null && product.getGameDetailsId() > 0) {
                        int gameDetailsId = product.getGameDetailsId();
                        request.setAttribute("gameKeys", new GameKeyDAO().findByGameDetailsId(gameDetailsId));

                        StorePlatformDAO platformDAO = new StorePlatformDAO();
                        List<StorePlatform> rawPlatforms = platformDAO.findByGameDetailsId(gameDetailsId);
                        Set<String> seenPlatformNames = new HashSet<>();
                        List<StorePlatform> distinctPlatforms = new ArrayList<>();
                        for (StorePlatform p : rawPlatforms) {
                            if (seenPlatformNames.add(p.getStoreOSName())) {
                                distinctPlatforms.add(p);
                            }
                        }
                        request.setAttribute("platforms", distinctPlatforms);

                        OperatingSystemDAO osDAO = new OperatingSystemDAO();
                        List<OperatingSystem> rawOs = osDAO.findByGameDetailsId(gameDetailsId);
                        Set<String> seenOsNames = new HashSet<>();
                        List<OperatingSystem> distinctOs = new ArrayList<>();
                        for (OperatingSystem os : rawOs) {
                            if (seenOsNames.add(os.getOsName())) {
                                distinctOs.add(os);
                            }
                        }
                        request.setAttribute("operatingSystems", distinctOs);
                    }

                    request.setAttribute("currencyFormatter", NumberFormat.getCurrencyInstance(Locale.US));
                    request.setAttribute("timestampFormatter", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
                    request.setAttribute("dateFormatter", new SimpleDateFormat("dd/MM/yyyy"));
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-details.jsp").forward(request, response);
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    Product productToDelete = productDAO.getProductById(id);
                    request.setAttribute("delete", productToDelete);
                    if (productToDelete != null) {
                        boolean isSold = productDAO.isProductSold(id);
                        request.setAttribute("isSold", isSold);
                    }
                    request.getRequestDispatcher("/WEB-INF/dashboard/product-delete.jsp").forward(request, response);
                    break;
                }
                default: { 

                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        if (session.getAttribute("successMessage") != null) {
                            request.setAttribute("successMessage", session.getAttribute("successMessage"));
                            session.removeAttribute("successMessage");
                        }
                         if (session.getAttribute("errorMessage") != null) {
                            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
                            session.removeAttribute("errorMessage");
                        }
                    }

                    ProductDAO productDAO = new ProductDAO();
                    List<Product> fullProductList;
                    String searchQuery = request.getParameter("query");

                    if ("search".equals(action) && searchQuery != null && !searchQuery.trim().isEmpty()) {
                        fullProductList = productDAO.searchProductsByName(searchQuery.trim());
                    } else {
                        fullProductList = productDAO.getAllProducts();
                    }

                    int page = 1;
                    try {
                        String pageStr = request.getParameter("page");
                        if (pageStr != null && !pageStr.isEmpty()) {
                            page = Integer.parseInt(pageStr);
                        }
                    } catch (NumberFormatException e) {
                        page = 1;
                    }

                    int totalProducts = fullProductList.size();
                    int totalPages = (int) Math.ceil((double) totalProducts / PRODUCTS_PER_PAGE);
                    if (totalPages == 0) totalPages = 1;
                    if (page < 1) page = 1;
                    if (page > totalPages) page = totalPages;

                    int start = (page - 1) * PRODUCTS_PER_PAGE;
                    int end = Math.min(start + PRODUCTS_PER_PAGE, totalProducts);
                    List<Product> pagedProducts = new ArrayList<>();
                    if (start < end) {
                        pagedProducts = fullProductList.subList(start, end);
                    }

                    String pageUrl;
                    if ("search".equals(action) && searchQuery != null && !searchQuery.isEmpty()) {
                        pageUrl = "manage-products?action=search&query=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8.toString()) + "&page=";
                    } else {
                        pageUrl = "manage-products?action=list&page=";
                    }
                    String previousPageUrl = (page > 1) ? pageUrl + (page - 1) : "#";
                    String nextPageUrl = (page < totalPages) ? pageUrl + (page + 1) : "#";

                    request.setAttribute("productList", pagedProducts);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("startRowNumber", start + 1);
                    request.setAttribute("pageUrl", pageUrl);
                    request.setAttribute("previousPageUrl", previousPageUrl);
                    request.setAttribute("nextPageUrl", nextPageUrl);
                    request.setAttribute("currentQuery", searchQuery);

                    request.getRequestDispatcher("/WEB-INF/dashboard/product-list.jsp").forward(request, response);
                    break;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error in doGet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Database error in doGet", e);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or page number format.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in doGet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("An unexpected error occurred", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
            return;
        }

        try {
            switch (action) {
                case "add": {
                    try {
                        String name = request.getParameter("name");
                        String priceStr = request.getParameter("price");
                        String description = request.getParameter("description");
                        String categoryIdStr = request.getParameter("categoryId");
                        String productType = request.getParameter("productType");

                        if (name == null || name.trim().isEmpty() || priceStr == null || priceStr.trim().isEmpty()
                                || categoryIdStr == null || categoryIdStr.trim().isEmpty() || productType == null || productType.trim().isEmpty()) {
                            throw new IllegalArgumentException("Please fill in all required fields: Name, Price, Product Type.");
                        }

                        BigDecimal price = new BigDecimal(priceStr);
                        int categoryId = Integer.parseInt(categoryIdStr);

                        if (price.compareTo(BigDecimal.ZERO) <= 0) {
                            throw new IllegalArgumentException("Price must be a positive value.");
                        }
                        
                        List<String> imageUrls = new ArrayList<>();
                        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        for (Part filePart : request.getParts()) {
                            if ("productImages".equals(filePart.getName()) && filePart.getSize() > 0) {
                                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                                if (fileName != null && !fileName.isEmpty()) {
                                    filePart.write(uploadPath + File.separator + fileName);
                                    imageUrls.add(fileName);
                                }
                            }
                        }

                        if (imageUrls.isEmpty()) {
                            throw new IllegalArgumentException("Product must have at least one image.");
                        }

                        Map<String, ProductAttribute> uniqueAttributes = new HashMap<>();
                        String[] customNames = request.getParameterValues("customAttributeNames");
                        String[] customValues = request.getParameterValues("customAttributeValues");
                        if (customNames != null && customValues != null) {
                            for (int i = 0; i < customNames.length; i++) {
                                String attrName = customNames[i];
                                String attrValue = customValues[i];
                                if (attrName != null && !attrName.trim().isEmpty() && attrValue != null && !attrValue.trim().isEmpty()) {
                                    uniqueAttributes.put(attrName.trim().toLowerCase(), new ProductAttribute(attrName.trim(), attrValue.trim()));
                                }
                            }
                        }

                        Product product = new Product();
                        product.setName(name);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setCategoryId(categoryId);
                        product.setActiveProduct(1);

                        ProductDAO productDAO = new ProductDAO();

                        if ("game".equalsIgnoreCase(productType)) {
                            product.setBrandId(null);
                            GameDetails gameDetails = new GameDetails();
                            gameDetails.setDeveloper(request.getParameter("developer"));
                            gameDetails.setGenre(request.getParameter("genre"));
                            String releaseDateStr = request.getParameter("releaseDate");
                            if (releaseDateStr == null || releaseDateStr.trim().isEmpty()) {
                                throw new IllegalArgumentException("Release Date is required for Games.");
                            }
                            gameDetails.setReleaseDate(Date.valueOf(releaseDateStr));
                            String[] platformIds = request.getParameterValues("platformIds");
                            String[] osIds = request.getParameterValues("osIds");
                            String newKeysRaw = request.getParameter("gameKeys");
                            String[] newKeys = (newKeysRaw != null && !newKeysRaw.trim().isEmpty()) ? newKeysRaw.split("\\r?\\n") : new String[0];
                            product.setQuantity(newKeys.length);
                            List<ProductAttribute> finalAttributes = new ArrayList<>(uniqueAttributes.values());
                            productDAO.addFullGameProduct(product, gameDetails, imageUrls, platformIds, osIds, newKeys, finalAttributes);
                        } else {
                            String quantityStr = request.getParameter("quantity");
                            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                                throw new IllegalArgumentException("Quantity is required for Accessories.");
                            }
                            int quantity = Integer.parseInt(quantityStr);
                            if (quantity <= 0) {
                                throw new IllegalArgumentException("Quantity must be a positive number for accessories.");
                            }
                            product.setQuantity(quantity);
                            String brandIdStr = request.getParameter("brandId");
                            product.setBrandId((brandIdStr != null && !brandIdStr.isEmpty()) ? Integer.parseInt(brandIdStr) : null);
                            String[] attrNames = {"Warranty", "Weight", "Connection Type", "Usage Time", "Headphone Type", "Material", "Battery Capacity", "Features", "Size", "Keyboard Type", "Mouse Type", "Charging Time"};
                            String[] paramNames = {"warrantyMonths", "weightGrams", "connectionType", "usageTimeHours", "headphoneType", "headphoneMaterial", "headphoneBattery", "headphoneFeatures", "keyboardSize", "keyboardType", "mouseType", "controllerChargingTime"};
                            for (int i = 0; i < paramNames.length; i++) {
                                String value = request.getParameter(paramNames[i]);
                                if (value != null && !value.trim().isEmpty()) {
                                    uniqueAttributes.put(attrNames[i].trim().toLowerCase(), new ProductAttribute(attrNames[i], value));
                                }
                            }
                            List<ProductAttribute> finalAttributes = new ArrayList<>(uniqueAttributes.values());
                            productDAO.addAccessoryProduct(product, finalAttributes, imageUrls);
                        }

                        // --- Success Redirect with Flash Message ---
                        request.getSession().setAttribute("successMessage", "Product added successfully.");
                        response.sendRedirect(request.getContextPath() + "/manage-products?action=list");

                    } catch (IllegalArgumentException e) {
                        System.err.println("Validation Error: " + e.getMessage());
                        request.setAttribute("errorMessage", e.getMessage());
                        request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                        request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                        request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                        request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());
                        request.getRequestDispatcher("/WEB-INF/dashboard/product-add.jsp").forward(request, response);
                    } catch (Exception e) {
                        System.err.println("Error adding product: " + e.getMessage());
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "An error occurred while creating the product: " + e.getMessage());
                        request.setAttribute("categoriesList", new CategoryDAO().getAllCategories());
                        request.setAttribute("brandsList", new BrandDAO().getAllBrands());
                        request.setAttribute("allPlatforms", new StorePlatformDAO().getAllPlatforms());
                        request.setAttribute("allOS", new OperatingSystemDAO().getAllOperatingSystems());
                        request.getRequestDispatcher("/WEB-INF/dashboard/product-add.jsp").forward(request, response);
                    }
                    break;
                }
                case "update": {
                    String name = request.getParameter("name");
                    String priceStr = request.getParameter("price");
                    String description = request.getParameter("description");
                    String categoryIdStr = request.getParameter("categoryId");
                    String productType = request.getParameter("productType");
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int oldquantity = Integer.parseInt(request.getParameter("oldquantity"));

                    if (name == null || name.trim().isEmpty()) {
                        throw new IllegalArgumentException("Product Name is required.");
                    }
                    if (priceStr == null || priceStr.trim().isEmpty()) {
                        throw new IllegalArgumentException("Price is required.");
                    }
                    if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                        throw new IllegalArgumentException("Product Type must be selected.");
                    }
                    if (productType == null || productType.trim().isEmpty()) {
                        throw new IllegalArgumentException("Product Type data is missing.");
                    }

                    BigDecimal price;
                    try {
                        price = new BigDecimal(priceStr);
                        if (price.compareTo(BigDecimal.ZERO) <= 0) {
                            throw new IllegalArgumentException("Price must be a positive value.");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Price must be a valid number.");
                    }
                    int categoryId = Integer.parseInt(categoryIdStr);

                    // --- 2. Xử lý và xác thực hình ảnh ---
                    List<String> finalImageUrls = new ArrayList<>();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    String[] originalImageViews = request.getParameterValues("originalImageViews");
                    if (originalImageViews != null) {
                        for (String imageUrl : originalImageViews) {
                            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                                finalImageUrls.add(imageUrl.trim());
                            }
                        }
                    }

                    for (Part filePart : request.getParts()) {
                        if ("productImages".equals(filePart.getName()) && filePart.getSize() > 0) {
                            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            if (fileName != null && !fileName.isEmpty()) {
                                filePart.write(uploadPath + File.separator + fileName);
                                finalImageUrls.add(fileName);
                            }
                        }
                    }
                    if (finalImageUrls.isEmpty()) {
                        throw new IllegalArgumentException("Product must have at least one image.");
                    }

                    // --- 3. Tạo đối tượng và thu thập dữ liệu theo loại ---
                    Product product = new Product();
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setCategoryId(categoryId);
                    product.setActiveProduct(1);

                    GameDetails gameDetails = null;
                    String[] platformIds = null, osIds = null, newKeys = null;
                    Map<String, ProductAttribute> uniqueAttributes = new HashMap<>();

                    if ("game".equalsIgnoreCase(productType)) {
                        // Validation dành riêng cho Game
                        String developer = request.getParameter("developer");
                        String genre = request.getParameter("genre");
                        String releaseDateStr = request.getParameter("releaseDate");
                        if (developer == null || developer.trim().isEmpty()) {
                            throw new IllegalArgumentException("Developer is required for games.");
                        }
                        if (genre == null || genre.trim().isEmpty()) {
                            throw new IllegalArgumentException("Genre is required for games.");
                        }
                        if (releaseDateStr == null || releaseDateStr.trim().isEmpty()) {
                            throw new IllegalArgumentException("Release Date is required for games.");
                        }

                        Date releaseDate;
                        try {
                            releaseDate = Date.valueOf(releaseDateStr);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Invalid Release Date format. Use YYYY-MM-DD.");
                        }

                        product.setBrandId(null);

                        int gameDetailsId = Integer.parseInt(request.getParameter("gameDetailsId"));
                        if (gameDetailsId > 0) {
                            product.setGameDetailsId(gameDetailsId);
                        }
                        gameDetails = new GameDetails(gameDetailsId, developer, genre, releaseDate);

                        platformIds = request.getParameterValues("platformIds");
                        osIds = request.getParameterValues("osIds");
                        String newKeysRaw = request.getParameter("newGameKeys");
                        if (newKeysRaw != null && !newKeysRaw.trim().isEmpty()) {
                            newKeys = newKeysRaw.split("\\r?\\n");
                        }else{
                            newKeys = new String[0];
                        }
                        int sum = oldquantity + newKeys.length;
                        product.setQuantity(sum);

                    } else { 
                        String quantityStr = request.getParameter("quantity");
                        if (quantityStr == null || quantityStr.trim().isEmpty() ) {
                            throw new IllegalArgumentException("Quantity is required for accessories.");
                        }
                        int quantity;
                        try {
                            quantity = Integer.parseInt(quantityStr);
                            if (quantity <= 0) {
                                throw new IllegalArgumentException("The quantity must be a non-negative number and equal to zero.");
                            }
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Quantity must be a valid number.");
                        }
                        product.setQuantity(quantity);
                        product.setGameDetailsId(null);

                        String brandIdStr = request.getParameter("brandId");
                        product.setBrandId((brandIdStr != null && !brandIdStr.isEmpty()) ? Integer.parseInt(brandIdStr) : null);
                    }

                    // --- 4. Thu thập và xác thực thuộc tính tùy chỉnh ---
                    String[] customNames = request.getParameterValues("customAttributeNames");
                    String[] customValues = request.getParameterValues("customAttributeValues");
                    if (customNames != null && customValues != null) {
                        for (int i = 0; i < customNames.length; i++) {
                            String attrName = customNames[i];
                            String attrValue = customValues[i];
                            boolean nameExists = attrName != null && !attrName.trim().isEmpty();
                            boolean valueExists = attrValue != null && !attrValue.trim().isEmpty();
                            if (nameExists && !valueExists) {
                                throw new IllegalArgumentException("Custom attribute '" + attrName + "' is missing a value.");
                            }
                            if (!nameExists && valueExists) {
                                throw new IllegalArgumentException("A custom attribute value '" + attrValue + "' is missing a name.");
                            }
                            if (nameExists) {
                                uniqueAttributes.put(attrName.trim().toLowerCase(), new ProductAttribute(attrName.trim(), attrValue.trim()));
                            }
                        }
                    }
                    List<ProductAttribute> finalAttributes = new ArrayList<>(uniqueAttributes.values());

                    // --- 5. Gọi DAO để cập nhật CSDL ---
                    ProductDAO productDAO = new ProductDAO();

                    product.setProductId(productId);
                    productDAO.updateProduct(product, gameDetails, finalAttributes, finalImageUrls, platformIds, osIds, newKeys);
                    
                    request.getSession().setAttribute("successMessage", "Product updated successfully.");
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    ProductDAO productDAO = new ProductDAO();
                    productDAO.deleteProduct(id);
                    request.getSession().setAttribute("successMessage", "Product deleted successfully.");
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
                }
                case "updateVisibility": {
                    int productId = Integer.parseInt(request.getParameter("id"));
                    int newStatus = Integer.parseInt(request.getParameter("newStatus"));
                    String page = request.getParameter("page");

                    ProductDAO productDAO = new ProductDAO();
                    productDAO.updateProductVisibility(productId, newStatus);

                    request.getSession().setAttribute("successMessage", "Product status updated successfully.");
                    String redirectUrl = request.getContextPath() + "/manage-products?action=list";
                    if (page != null && !page.isEmpty()) {
                        redirectUrl += "&page=" + page;
                    }

                    response.sendRedirect(redirectUrl);
                    break;
                }
                default:
                    response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
                    break;
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in doPost (main catch): " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/manage-products?action=list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles all product management actions.";
    }
}