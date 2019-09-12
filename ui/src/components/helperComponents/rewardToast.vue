<template>
    <div>
        <div v-if="pointsRewarded">
            <p>You gained {{pointsRewarded.value}} {{pointsRewarded.value === 1 ? 'point!' : 'points!'}}</p>
        </div>
        <b-list-group v-if="badgeAchieved">
            <b-list-group-item
                               class="flex-column align-items-start"
                               v-if="badgeAchieved !== null"
                               :key="badgeAchieved.id">
                <b-row>
                    <b-col cols="4">
                        <single-badge :badge="badgeAchieved"></single-badge>
                    </b-col>
                    <b-col>
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">{{badgeAchieved.name}}</h5>
                            <small>{{badgeAchieved.progress}}{{badgeAchieved.breakpoint == null ? '' : '/' + badgeAchieved.breakpoint}}</small>
                        </div>

                        <p class="mb-1">
                            {{badgeAchieved.howToProgress}}
                        </p>

                        <b-progress
                            :value="badgeAchieved.progress"
                            :max="badgeAchieved.breakpoint == null ? badgeAchieved.progress : badgeAchieved.breakpoint"
                            :variant="badgeAchieved.breakpoint == null ? 'success' : 'primary'">
                        </b-progress>
                    </b-col>
                </b-row>
            </b-list-group-item>
        </b-list-group>
    </div>
</template>

<script>
    import SingleBadge from "../badges/singleBadge";
    export default {
        name: "rewardToast",

        props: {
            pointsRewarded: Object,
            badgeAchieved: Object
        },

        data() {
            return {
                rewardLevels: ['Bronze', 'Silver', 'Gold']
            }
        },

        components: {
            SingleBadge
        },


    }
</script>