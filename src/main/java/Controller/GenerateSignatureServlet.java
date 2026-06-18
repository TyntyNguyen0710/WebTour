package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@WebServlet("/GenerateSignatureServlet")
public class GenerateSignatureServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String invoiceHash = request.getParameter("invoiceHash");
        String privateKeyPem = request.getParameter("privateKeyPem");
        String bookingId = request.getParameter("bookingId");

        try {
            PrivateKey privateKey = getPrivateKeyFromPem(privateKeyPem);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(invoiceHash.getBytes("UTF-8"));

            byte[] digitalSignature = signature.sign();
            String signatureBase64 = Base64.getEncoder().encodeToString(digitalSignature);

            request.setAttribute("invoiceHash", invoiceHash);
            request.setAttribute("digitalSignature", signatureBase64);
            request.setAttribute("bookingId", bookingId);

            request.getRequestDispatcher("signatureResult.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Lỗi khi ký hóa đơn!</h3><p>" + e.getMessage() + "</p>");
        }
    }

    private PrivateKey getPrivateKeyFromPem(String pem) throws Exception {
        String privateKeyPEM = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}