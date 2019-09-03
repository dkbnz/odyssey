<template>
    <div>
        <b-list-group class="scroll">
            <b-list-group-item href="#"
                               class="flex-column align-items-start"
                               v-for="profile in profileList"
                               :key="profile.id"
                               @click="$emit('profile-click', profile)">
                <b-row>
                    <b-col cols="4">
                        <b-img :src="getProfilePictureThumbnail(profile.profilePicture)"
                               onerror="this.src = '../../../static/default_profile_picture.png'"
                               fluid
                               rounded="circle"
                               thumbnail
                               alt="Profile Image">
                        </b-img>
                    </b-col>
                    <b-col>
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">{{profile.firstName}} {{profile.lastName}}</h5>
                            <small>#{{profile.achievementTracker.rank}} ({{ profile.achievementTracker.points}})</small>
                        </div>

                        <p class="mb-1">
                            We need to decide what to put here, we could add some badges, or some nationalities, or traveller types. The world is our oyster. Heck, we could even put the current weather here if we wanted to.
                        </p>
                    </b-col>
                </b-row>
            </b-list-group-item>
        </b-list-group>
    </div>
</template>

<script>
    export default {
        name: "mobileProfileList",
        props: {
            profileList: Array,
            loading: {
                default: function() {
                    return false;
                }
            }
        },
        methods: {
            /**
             * Retrieves the user's primary photo thumbnail, if none is found set to the default image.
             * @param photo         returns a url of which photo should be displayed as the profile picture for the user.
             */
            getProfilePictureThumbnail(photo) {
                if (photo !== null) {
                    let photoId = photo.id;
                    return `/v1/photos/thumb/` + photoId;
                } else {
                    return "/static/default_profile_picture.png";
                }
            }
        }
    }
</script>

<style scoped>

</style>