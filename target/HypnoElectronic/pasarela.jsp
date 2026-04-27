<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Pasarela de Pago Segura - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body { background-color: #0a0a0a; color: white; display: flex; align-items: center; min-height: 100vh; font-family: 'Segoe UI', sans-serif; }
        .gateway-card { background: #111; border: 1px solid #333; border-radius: 20px; overflow: hidden; }
        .neon-border { border: 1px solid var(--neon-blue); box-shadow: 0 0 15px rgba(0, 212, 255, 0.2); }
        .credit-card-sim { background: linear-gradient(135deg, #00d4ff 0%, #0055ff 100%); border-radius: 15px; height: 180px; position: relative; padding: 20px; color: white; }
        .chip { width: 40px; height: 30px; background: #ffd700; border-radius: 5px; margin-bottom: 20px; }
        :root { --neon-blue: #00d4ff; }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="gateway-card shadow-lg">
                    <div class="p-4 text-center border-bottom border-secondary">
                        <h4 class="mb-0"><i class="bi bi-shield-lock-fill text-success"></i> Pago Seguro</h4>
                        <p class="small text-secondary mb-0">HypnoElectronic Checkout</p>
                    </div>
                    
                    <div class="p-4">
                        <!-- Simulación de Tarjeta -->
                        <div class="credit-card-sim mb-4 shadow">
                            <div class="d-flex justify-content-between">
                                <div class="chip"></div>
                                <i class="bi bi-wifi" style="transform: rotate(90deg);"></i>
                            </div>
                            <div class="h4 mb-3 letter-spacing-2">**** **** **** 1234</div>
                            <div class="d-flex justify-content-between small">
                                <span>${sessionScope.user.fullName.toUpperCase()}</span>
                                <span>12/28</span>
                            </div>
                            <div class="position-absolute bottom-0 end-0 p-3">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/5/5e/Visa_Inc._logo.svg" height="20">
                            </div>
                        </div>

                        <div class="mb-4">
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-secondary small">Concepto:</span>
                                <span class="small">Compra de Periféricos</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="text-secondary fw-bold">TOTAL A DEBITAR:</span>
                                <h3 class="text-info mb-0">$${sessionScope.totalFinal}</h3>
                            </div>
                        </div>

                        <form action="confirmar-pago" method="post" id="payForm">
                            <button type="submit" class="btn btn-info btn-lg w-100 fw-bold py-3 shadow" onclick="this.innerHTML='PROCESANDO...'; this.disabled=true; document.getElementById('payForm').submit();">
                                <i class="bi bi-credit-card"></i> CONFIRMAR PAGO
                            </button>
                        </form>

                        <div class="mt-4 text-center">
                            <div class="row g-2 opacity-50">
                                <div class="col-4 small"><i class="bi bi-lock"></i> SSL</div>
                                <div class="col-4 small"><i class="bi bi-check-circle"></i> PCI-DSS</div>
                                <div class="col-4 small"><i class="bi bi-shield-shaded"></i> AES-256</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="text-center mt-3">
                    <a href="checkout" class="text-secondary small text-decoration-none">← Cancelar y volver</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>