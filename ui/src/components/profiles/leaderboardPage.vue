<template>
    <div v-if="profile.length !== 0">
        <navbar-main v-bind:profile="profile" v-if="!minimalInfo"></navbar-main>

        <div class="d-block d-lg-none">
            <mobile-leaderboard
                    :profile="profile"
            ></mobile-leaderboard>
        </div>

        <div class="d-none d-lg-block">
            <desktop-leaderboard
                    :profile="profile"
                    :minimal-info="minimalInfo"
                    :destinations="destinations"
            ></desktop-leaderboard>
        </div>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import NavbarMain from "../helperComponents/navbarMain";
    import MobileLeaderboard from "./mobileLeaderboard";
    import DesktopLeaderboard from "./desktopLeaderboard";
    import UnauthorisedPrompt from "../helperComponents/unauthorisedPromptPage";

    export default {
        name: "leaderboardPage",
        components: {
            UnauthorisedPrompt,
            DesktopLeaderboard,
            MobileLeaderboard,
            NavbarMain
        },
        props: {
            profile: Object,
            minimalInfo: {
                default: function () {
                    return false;
                }
            },
            destinations: Array,
            containerClass: {
                default: function () {
                    return 'containerMain';
                }
            }
        }
    }
</script>

<style scoped>

</style>