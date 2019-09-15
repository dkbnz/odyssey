<template>
    <div class="scroll">
        <b-list-group>
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
                            <h5 class="mb-1 mobileLeaderboard"><b>{{profile.firstName}} {{profile.lastName}}</b></h5>
                            <small>#{{profile.achievementTracker.rank}} ({{ profile.achievementTracker.points}})</small>
                        </div>
                        <div>
                            <p class="wrapWhiteSpaceSmallGap">{{calculateNationalities(profile)}}</p>
                            <small>Badges Achieved: {{profile.achievementTracker.badges.length}} <br />
                                Points: {{profile.achievementTracker.points}} <br />
                                Quests Created: {{profile.numberOfQuestsCreated}} <br />
                                Quests Completed: {{profile.numberOfQuestsCompleted}} <br />
                            </small>
                        </div>

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
             *
             * @param photo     returns a url of which photo should be displayed as the profile picture for the user.
             * @return          string value containing the url to load the profile thumbnail.
             */
            getProfilePictureThumbnail(photo) {
                if (photo !== null) {
                    let photoId = photo.id;
                    return `/v1/photos/thumb/` + photoId;
                } else {
                    return "/static/default_profile_picture.png";
                }
            },


            /**
             * Used to calculate a specific profiles nationalities from their list of nationalities. Shows all the
             * nationalities in the row.
             *
             * @param profile     the profile to be calculated for nationalities.
             */
            calculateNationalities(profile) {
                let nationalityList = "";
                for (let i = 0; i < profile.nationalities.length; i++) {
                    if (profile.nationalities[i + 1] !== undefined) {
                        nationalityList += profile.nationalities[i].nationality + ", \n";
                    } else {
                        nationalityList += profile.nationalities[i].nationality;
                    }

                }
                return nationalityList;
            },
        }
    }
</script>