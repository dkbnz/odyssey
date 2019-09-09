<template>
    <div>
        <b-card>
            <b-img :src="assets[sourceBadge]" :class="{disabledBadge: !achievedBadge}"></b-img>
            <div class="rowStars">
                <div class="columnStars" v-for="index in 3" :key="index">
                    <b-img
                            :id="'star-' + index"
                            :src="assets['star']"
                            :class="{disabledBadge: index > achievementLevel}">
                    </b-img>
                </div>
            </div>
        </b-card>

    </div>
</template>

<script>
    import assets from '../../assets/assets';
    export default {
        name: "singleBadge",

        props: {
            sourceBadge: String,
            achievedBadge: Boolean,
            achievementLevel: Number
        },

        computed: {
            assets() {
                return assets
            }
        }
    }
</script>