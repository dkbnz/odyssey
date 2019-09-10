<template>
    <div>
        <!--<p>{{rewardJson.pointsRewarded}}</p>-->
        {{rewardJson}}
        <!--<div v-for="badge in rewardJson.badgesAchieved">-->
            <!--<p>{{badge}}</p>-->
        <!--</div>-->
    </div>
</template>

<script>
    export default {
        props: {
            rewardJson: Object
        },
        name: "rewardToast"
    }
</script>

<style scoped>

</style>