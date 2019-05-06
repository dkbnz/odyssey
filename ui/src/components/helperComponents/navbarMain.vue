<template>
    <div>
        <b-navbar variant="light" toggleable="lg">
            <b-navbar-brand v-on:click="goToProfile()"><img :src="assets.appLogo" ></b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item :class="{active: currentPage==='/profiles'}" v-on:click="goToPeople()">People
                    </b-nav-item>
                    <b-nav-item :class="{active: currentPage==='/trips'}" v-on:click="goToTrips()">Trips</b-nav-item>
                    <b-nav-item :class="{active: currentPage==='/destinations'}" v-on:click="goToDestinations()">
                        Destinations
                    </b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav class="ml-auto">
                    <b-nav-item-dropdown right>
                        <!-- Using 'button-content' slot -->
                        <template slot="button-content"><em>{{ profile.firstName }}</em></template>
                        <b-dropdown-item :class="{active: currentPage==='/dash'}" v-on:click="goToProfile()">Profile
                        </b-dropdown-item>
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
            assets() {
                return assets
            },
        },
        data() {
            return {
                currentPage: '/dash'
            }
        },
        mounted() {
            this.getCurrentPage();
        },
        methods: {
            /**
             * Logs the user out and returns to the index page
             */
            logout() {
                let self = this;
                fetch(`/v1/logout`, {
                    method: 'POST',
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(function (response) {
                        if (response.ok) {
                            self.$router.push("/");
                            self.$router.go();
                            return response;
                        } else {
                            self.$router.push("/dash");
                            return response;
                        }
                    });
            },
            getCurrentPage() {
                this.currentPage = this.$router.currentRoute.fullPath;
            },
            goToPeople() {
                this.$router.push("/profiles");
            },
            goToTrips() {
                this.$router.push("/trips");
            },
            goToDestinations() {
                this.$router.push("/destinations");
            },
            goToProfile() {
                this.$router.push("/dash");
            }
        }
    }
</script>