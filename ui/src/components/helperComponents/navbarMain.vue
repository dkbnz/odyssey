<template>
    <div>
        <b-navbar class="mainNav" toggleable="lg" variant="light">
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            <router-link :to="'/profile'" tag="b-navbar-brand" class="nav-bar-brand" :class="{active: currentPage ==='/trips'}">
                <img :src="assets.appLogo" width="200px">
            </router-link>
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <router-link class="d-none d-lg-block" :to="'/trips'" tag="b-nav-item" :class="{active: currentPage ==='/trips'}">Trips</router-link>
                    <router-link class="d-none d-lg-block" :to="'/destinations'" tag="b-nav-item" :class="{active: currentPage ==='/destinations'}">Destinations</router-link>
                    <router-link :to="'/quests'" tag="b-nav-item" :class="{active: currentPage ==='/quests'}">Quests</router-link>
                    <router-link :to="'/leaderboard'" tag="b-nav-item" :class="{active: currentPage ==='/leaderboard'}">Leaderboard</router-link>
                    <router-link class="d-none d-lg-block" :to="'/admin'" tag="b-nav-item" :class="{active: currentPage ==='/admin'}" v-if="profile.admin">Admin</router-link>
                </b-navbar-nav>

                <b-navbar-nav class="ml-auto">
                    <router-link :to="'/profile'" tag="b-nav-item" :class="{active: currentPage ==='/profile'}">{{ profile.firstName }}</router-link>
                    <b-nav-item @click="logout">
                        Logout
                    </b-nav-item>
                </b-navbar-nav>

            </b-collapse>
        </b-navbar>
    </div>
</template>

<script>
    export default {
        name: "navbarMain",

        props: ['profile'],

        data() {
            return {
                currentPage: '/profile'
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
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response;
                    }
                }).then(function (response) {
                    self.$router.push("/");
                    return response;
                }).catch(function (response) {
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Used to determine the current page, so current page detection is shown in the nav bar.
             */
            getCurrentPage() {
                this.currentPage = this.$router.currentRoute.fullPath;
            }
        }
    }
</script>