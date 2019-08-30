<template>
    <div>
        <b-table :busy="loading"
                 :fields="fields"
                 :items="profileList"
                 responsive
                 hover
                 id="profiles"
                 outlined
                 ref="profilesTable"
                 striped>

            <!--:per-page="perPage"-->
            <!--:sort-by.sync="sortBy"-->
            <!--:sort-desc.sync="sortDesc"-->

            <div class="text-center my-2" slot="table-busy">
                <b-spinner v-if="loading"></b-spinner>
            </div>
            <template slot="profilePhoto" slot-scope="row">
                <b-img :src="getProfilePictureThumbnail(row.item.profilePicture)"
                       onerror="this.src = '../../../static/default_profile_picture.png'"
                       fluid
                       rounded="circle"
                       thumbnail
                       alt="Profile Image">
                </b-img>
            </template>

            <!--Shows more details about any profile-->
            <template slot="actions" slot-scope="row">
                <!-- If user is admin, can delete, make/remove admin rights and delete other users -->
                <b-row class="text-center" v-if="profile.admin && adminView">
                    <b-button @click="$emit('make-admin', row.item)" block
                              class="mr-2" size="sm"
                              v-if="!row.item.admin"
                              variant="success">
                        Make Admin
                    </b-button>
                    <b-button @click="$emit('remove-admin', row.item)" block class="mr-2"
                              size="sm" v-if="row.item.admin && row.item.id !== 1"
                              variant="danger">
                        Remove Admin
                    </b-button>
                    <b-button @click="$emit('admin-edit', row.item)" block class="mr-2" size="sm" variant="warning">
                        Show More Details
                    </b-button>
                    <b-button @click="$emit('profile-delete', row.item)"
                              block class="mr-2" size="sm"
                              v-b-modal.deleteProfileModal v-if="row.item.id !== 1"
                              variant="danger">
                        Delete
                    </b-button>
                </b-row>
                <!-- If user is not admin, can only see other profiles -->
                <b-row class="text-center" v-else>
                    <b-button @click="row.toggleDetails" block class="mr-2" size="sm" variant="warning">
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} More Details
                    </b-button>
                </b-row>
            </template>

            <template slot="row-details" slot-scope="row">
                <b-card bg-variant="secondary">
                    <view-profile
                            :containerClass="'profilesSubSectionProfile'"
                            :containerClassContent="'profilesSubSectionContent'"
                            :admin-view="profile.admin"
                            :destinations="destinations"
                            :profile="row.item"
                            :userProfile="profile">
                    </view-profile>
                </b-card>
            </template>

        </b-table>
        <div class="text-center my-2" v-if="profileList.length === 0 && !loading">
            <strong>Can't find any profiles!</strong>
        </div>


        <!--Pagination and results per page settings-->
        <!--<b-row>-->
            <!--<b-col cols="1">-->
                <!--<b-form-group-->
                        <!--id="profiles-field"-->
                        <!--label-for="perPage">-->
                    <!--<b-form-select :options="optionViews" id="perPage"-->
                                   <!--size="sm"-->
                                   <!--trim-->
                                   <!--v-model="perPage"></b-form-select>-->
                <!--</b-form-group>-->
            <!--</b-col>-->
            <!--<b-col cols="8">-->
                <!--<b-pagination-->
                        <!--:per-page="perPage"-->
                        <!--:total-rows="rows"-->
                        <!--align="center"-->
                        <!--aria-controls="my-table"-->
                        <!--first-text="First"-->
                        <!--last-text="Last"-->
                        <!--size="sm"-->
                        <!--v-model="currentPage"-->
                <!--&gt;</b-pagination>-->
            <!--</b-col>-->
        <!--</b-row>-->

    </div>
</template>

<script>
    import ViewProfile from "../dash/viewProfile";
    export default {
        name: "profileList",
        components: {ViewProfile},
        props: {
            profileList: Array, // List of profiles to display
            adminView: Boolean,
            profile: Object, // Profile viewing the list
            loading: {
                default: function() {
                    return false;
                }
            }
        },
        computed: {
            fields() {
                return [
                    {key: 'achievements.ranking', label: "Rank", sortable: true, class: 'tableWidthSmall'},
                    {key: 'achievements.points', label: "Points", sortable: true, class: 'tableWidthSmall'},
                    {key: 'profilePhoto', label: "Photo", sortable: true, class: 'tableWidthSmall'},
                    {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'actions', class: 'tableWidthMedium'}
                    ]
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
                    return "../../../static/default_profile_picture.png";
                }
            }
        }
    }
</script>