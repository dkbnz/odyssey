<template>
    <footer class="d-none d-lg-block" id="footer">
        <p>This is Everyware's TravelEA Website!<br/>
            <b-button variant="link" class="footer-link" @click="goToCredits()">Credits</b-button>
        </p>
    </footer>
</template>

<script>
    export default {
        name: "footerMain",

        methods: {
            goToCredits() {
                this.$router.push('/credits')
            }
        }
    }
</script>