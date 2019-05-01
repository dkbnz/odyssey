<template>
    <div>
        <b-navbar variant="light" toggleable="lg">
            <b-navbar-brand href="/dash"><img :src="assets.appLogo"></b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item href="/profiles">People</b-nav-item>
                    <b-nav-item href="/trips">Trips</b-nav-item>
                    <b-nav-item href="/destinations">Destinations</b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav class="ml-auto">
                    <b-nav-item-dropdown right>
                        <!-- Using 'button-content' slot -->
                        <template slot="button-content"><em>{{ profile.firstName }}</em></template>
                        <b-dropdown-item href="/dash">Profile</b-dropdown-item>
                        <b-dropdown-item @click="logout">Logout</b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>

            </b-collapse>
        </b-navbar>
    </div>
</template>

<script>
    import assets from '../../assets/index'
    export default {
        name: "navbarMain",
        props: ['profile'],
        computed: {
            assets () {
                return assets
            },
        },
        methods: {
            logout () {
                let response = fetch(`/v1/logout`, {
                    method: 'POST',
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                .then(function(response) {
                    if(response.ok) {
                        window.location.pathname="/";
                        return response;
                    } else {
                        window.location.pathname="/dash";
                        return response
                    }
                });

            },
        }
    }
</script>

<style scoped>

</style>