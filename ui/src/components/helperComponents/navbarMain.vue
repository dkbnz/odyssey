<template>
    <div>
        <b-navbar class="mainNav" toggleable="lg" variant="light">
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            <b-navbar-brand class="nav-bar-brand" @click="goToProfile()"><img :src="assets.appLogo"></b-navbar-brand>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item class="d-none d-lg-block" :class="{active: currentPage==='/profiles'}"
                                @click="goToPeople()">People
                    </b-nav-item>
                    <b-nav-item class="d-none d-lg-block" :class="{active: currentPage==='/trips'}"
                                @click="goToTrips()">Trips
                    </b-nav-item>
                    <b-nav-item class="d-none d-lg-block" :class="{active: currentPage==='/destinations'}"
                                @click="goToDestinations()">
                        Destinations
                    </b-nav-item>
                    <b-nav-item class="d-none d-lg-block" :class="{active: currentPage==='/objectives'}" @click="goToObjectives()">Objectives
                    </b-nav-item>
                    <b-nav-item :class="{active: currentPage==='/quests'}" @click="goToQuests()">Quests</b-nav-item>
                    <b-nav-item class="d-none d-lg-block" :class="{active: currentPage==='/admin'}"
                                @click="goToAdminPanel()"
                                v-if="profile.admin">
                        Admin
                    </b-nav-item>
                </b-navbar-nav>

                <b-navbar-nav class="ml-auto">
                    <b-nav-item right :class="{active: currentPage==='/dash'}" @click="goToProfile()">
                        {{ profile.firstName }}
                    </b-nav-item>
                    <b-nav-item @click="logout">
                        Logout
                    </b-nav-item>
                </b-navbar-nav>

            </b-collapse>
        </b-navbar>
    </div>
</template>

<script>
    import Assets from '../../assets/index'

    export default {
        name: "navbarMain",

        props: ['profile'],

        computed: {
            assets() {
                return Assets
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
             * Logs the user out and returns to the index page.
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


            /**
             * Used to determine the current page, so current page detection is shown in the nav bar.
             */
            getCurrentPage() {
                this.currentPage = this.$router.currentRoute.fullPath;
            },


            /**
             * Methods to navigate to each page using the VueRouter.
             */
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
            },
            goToAdminPanel() {
                this.$router.push("/admin");
            },
            goToObjectives() {
                this.$router.push("/objectives");
            },
            goToQuests() {
                this.$router.push("/quests");
            }
        }
    }
</script>