<template>
    <footer class="d-none d-lg-block" id="footer">
        This is Everyware's Odyssey Website!<br/>
        <b-button variant="link" class="footer-link" @click="goToCredits()">Credits</b-button>

    </footer>
</template>

<script>
    export default {
        name: "footerMain",

        methods: {
            /**
             * Navigates to the credits pages, and scrolls to the top of the page.
             */
            goToCredits() {
                this.$router.push('/credits');
                window.scrollTo(0, 0);
            }
        }
    }
</script>