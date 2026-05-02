//Stripe の公開可能キーをセットする
const stripe = Stripe('pk_test_51Sm2o9CfS8tgiRY81DRaZxmHFXA2HBXDzmDFxxnxjpU9g0yC5xoR7RM2Dd1aTKiTbWVKcjk13fJ47jc0VElsCnx000abhEtPNE');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});