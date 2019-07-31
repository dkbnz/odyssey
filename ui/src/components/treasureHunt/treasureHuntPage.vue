<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for treasure hunt page-->
        <navbar-main v-bind:profile="profile" v-if="!adminView"></navbar-main>
        <div class="containerMain">
            <h1 class="page-title">Treasure Hunts</h1>
            <p class="page-title">
                <i>Here you can view and create Treasure hunts!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card>
                        <b-tabs content-class="mt-3">
                            <b-tab title="Available Treasure Hunts" active>
                                <treasure-hunt-list
                                        :profile="profile"
                                        :adminView="adminView"
                                        :yourTreasureHunts="false"
                                ></treasure-hunt-list>
                            </b-tab>
                            <b-tab title="Your Treasure Hunts">
                                <treasure-hunt-list
                                        :profile="profile"
                                        :adminView="adminView"
                                        :yourTreasureHunts="true"
                                ></treasure-hunt-list>
                            </b-tab>
                        </b-tabs>
                    </b-card>
                </b-col>
                <b-col>
                    <b-card>
                        <found-destinations
                                :search-public="true"
                                :profile="profile"
                                @destination-click="destination => $emit('destination-click', destination)">
                        </found-destinations>
                    </b-card>
                </b-col>
            </b-row>
            <footer-main></footer-main>
        </div>
    </div>
    <div v-else>
        <unauthorised-prompt-page></unauthorised-prompt-page>
    </div>
</template>

<script>
    import NavbarMain from "../helperComponents/navbarMain";
    import UnauthorisedPromptPage from "../helperComponents/unauthorisedPromptPage";
    import FoundDestinations from "../destinations/destinationSearchList";
    import TreasureHuntList from "./treasureHuntList";
    import FooterMain from "../helperComponents/footerMain";
    export default {
        name: "treasureHuntPage",

        props: {
            profile: Object,
            containerClass: {
                default: function() {
                    return null;
                }
            },
            adminView: {
                default: function() {
                    return false;
                }
            },
        },

        components: {
            FooterMain,
            TreasureHuntList,
            FoundDestinations,
            UnauthorisedPromptPage,
            NavbarMain,
        }
    }
</script>

<style scoped>

</style>