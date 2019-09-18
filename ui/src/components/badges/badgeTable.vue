<template>
    <div class="badgesContent">
        <div v-if="profile && profile.achievementTracker">
            <b-list-group >
                <b-list-group-item
                        class="flex-column align-items-start"
                        v-for="badge in profile.achievementTracker.badges"
                        :key="badge.id">
                    <b-row>
                        <b-col cols="4">
                            <single-badge :badge="badge"></single-badge>
                        </b-col>
                        <b-col>
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">{{badge.name}}</h5>
                                <small>{{badge.progress}}{{badge.breakpoint == null ? '' : '/' + badge.breakpoint}}</small>
                            </div>

                            <p class="mb-1">
                                {{badge.howToProgress}}
                            </p>

                            <b-progress :value="badge.progress" :max="badge.breakpoint == null ? badge.progress : badge.breakpoint" :variant="badge.breakpoint == null ? 'success' : 'primary'"></b-progress>
                        </b-col>
                    </b-row>
                </b-list-group-item>
            </b-list-group>
        </div>
        <div v-if="badges.length">
            <b-list-group-item class="flex-column align-items-start"
                               v-for="badge in badges"
                               :key="badge.id">
                <b-row>
                    <b-col cols="4">
                        <single-badge :badge="badge"></single-badge>
                    </b-col>
                    <b-col>
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">{{badge.name}}</h5>
                            <small>{{badge.progress}}{{badge.breakpoint == null ? '' : '/' + badge.breakpoint}}</small>
                        </div>

                        <p class="mb-1">
                            {{requiredActions[badge.actionToAchieve]}}
                        </p>
                    </b-col>
                </b-row>
            </b-list-group-item>
        </div>
        <div v-else class="text-center my-2" >
            <b-img alt="Loading" class="loading" v-if="loadingResults" :src="assets['loadingLogo']"></b-img>
        </div>
    </div>
</template>

<script>
    import SingleBadge from "./singleBadge";
    export default {
        name: "badgeList",

        props: {
            profile: Object
        },

        mounted() {
            this.getProfileBadges();
            this.getAllBadges();
        },

        watch: {
            profile() {
                this.getProfileBadges();
                this.getAllBadges();
            }
        },

        data() {
            return {
                badges: [],
                loadingResults: false,
                profileBadgesIds: [],
                requiredActions: {
                    DESTINATION_CREATED: 'This badge is awarded to those who\'ve created a destination!',
                    QUEST_CREATED: 'Create a quest of your own to earn this badge!',
                    TRIP_CREATED: 'Make a trip, and this badge will be yours!',
                    QUEST_COMPLETED: 'Those who have completed a quest can gain this badge!',
                    INTERNATIONAL_QUEST_COMPLETED: 'Complete an international quest and this badge is yours!',
                    LARGE_QUEST_COMPLETED: 'Quests come in all sizes, complete a large quest to earn this badge!',
                    DISTANCE_QUEST_COMPLETED: 'This badge is earned by those who go the extra mile, or 60.',
                    POINTS_GAINED: 'Gain some points to achieve this badge!',
                    LOGIN_STREAK: 'Come back tomorrow to earn this badge!',
                }
            }
        },

        components: {
            SingleBadge
        },

        methods: {
            /**
             * Retrieves all the possible badges stored in the database. Uses the getProfileBadges() method to determine
             * if the user has the badge in their list of badges. If they don't then displays the badge underneath each
             * the list of profile's badges.
             */
            getAllBadges() {
                this.loadingResults = true;
                let self = this;
                fetch(`/v1/achievementTracker/badges`, {
                    accept: 'application/json'

                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    for (let i = 0; i < responseBody.length; i++) {
                        if (!self.profileBadgesIds.includes(responseBody[i].id)) {
                            self.badges.push(responseBody[i]);
                        }
                    }
                }).catch(function (response) {
                    self.loadingResults = false;
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Gets all the profile badges id number to work out what badges to be displayed as disabled.
             */
            getProfileBadges() {
                let profileBadges = this.profile.achievementTracker.badges;
                for (let i = 0; i < profileBadges.length; i++) {
                    this.profileBadgesIds.push(profileBadges[i].id)
                }
            }
        }
    }
</script>