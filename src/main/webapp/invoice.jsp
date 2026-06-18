<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn đặt tour - Chữ ký số</title>
    <link rel="stylesheet" href="css/reset.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .invoice-box { max-width: 800px; margin: auto; padding: 30px; border: 1px solid #ddd; }
        .signature-section { margin-top: 30px; padding: 20px; background: #f9f9f9; border-radius: 8px; }
        textarea { width: 100%; height: 150px; font-family: monospace; }
        .hash { font-family: monospace; background: #eee; padding: 10px; word-break: break-all; }
    </style>
</head>
<body>
<div class="invoice-box">
    <h2>HÓA ĐƠN ĐẶT TOUR</h2>
    <hr>
    
    <p><strong>Mã booking:</strong> ${booking.id}</p>
    <p><strong>Khách hàng:</strong> ${customer.name}</p>
    <p><strong>Email:</strong> ${customer.email}</p>
    <p><strong>Số điện thoại:</strong> ${customer.phone}</p>
    
    <h3>Thông tin Tour</h3>
    <p><strong>Tên tour:</strong> ${tour.name}</p>
    <p><strong>Ngày khởi hành:</strong> <fmt:formatDate value="${booking.departureDate}" pattern="dd/MM/yyyy"/></p>
    <p><strong>Số người lớn:</strong> ${booking.noAdults}</p>
    <p><strong>Số trẻ em:</strong> ${booking.noChildren}</p>
    <p><strong>Giá tour:</strong> <fmt:formatNumber value="${tour.price}" type="currency" currencyCode="VND"/></p>
    
    <hr>
    <h3>Băm hóa đơn (SHA-256)</h3>
    <div class="hash">${invoiceHash}</div>

    <div class="signature-section">
        <h3>Ký hóa đơn bằng khóa bí mật (Private Key)</h3>
        <form action="GenerateSignatureServlet" method="post">
            <input type="hidden" name="bookingId" value="${booking.id}">
            <input type="hidden" name="invoiceHash" value="${invoiceHash}">
            
            <label><strong>Dán Private Key (PEM format):</strong></label><br>
            <textarea name="privateKeyPem" placeholder="-----BEGIN PRIVATE KEY-----&#10;...&#10;-----END PRIVATE KEY-----" required></textarea>
            
            <br><br>
            <button type="submit" style="padding: 10px 20px; background: #3498db; color: white; border: none; cursor: pointer;">
                Ký hóa đơn (Tạo chữ ký số)
            </button>
        </form>
    </div>
</div>
</body>
</html>