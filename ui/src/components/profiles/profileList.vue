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
                 :no-local-sorting= 'true'
                 @sort-changed="sortingChanged"
                 striped>

            <div class="text-center my-2" slot="table-busy">
                <b-img alt="Loading" class="loading" v-if="loading" :src="assets['loadingLogo']"></b-img>
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
            <template slot="nationalities" slot-scope="row">
                <p class="wrapWhiteSpace">{{calculateNationalities(row.item.nationalities)}}</p>
            </template>

            <template slot="travellerTypes" slot-scope="row" class="wrapWhiteSpace">
                <p class="wrapWhiteSpace">{{calculateTravellerTypes(row.item.travellerTypes)}}</p>
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
                <b-card bg-variant="light">
                    <b-row>
                        <b-col cols="3">
                            <h3>Information</h3>
                            <p>Date of Birth: {{new Date(row.item.dateOfBirth).toLocaleDateString()}}</p>
                            <h3>Statistics</h3>
                            <p>Badges Achieved: {{row.item.achievementTracker.badges.length}} <br />
                                Points: {{row.item.achievementTracker.points}} <br />
                                Quests Created: {{row.item.numberOfQuestsCreated}} <br />
                                Quests Completed: {{row.item.numberOfQuestsCompleted}} <br />
                            </p>
                        </b-col>
                        <b-col cols="3">
                            <h3>Nationalities</h3>
                            <ul>
                                <li v-for="nationality in row.item.nationalities">{{nationality.nationality}}</li>
                            </ul>
                            <h3>Passports</h3>
                            <ul>
                                <li v-for="passport in row.item.passports">{{passport.country}}</li>
                            </ul>
                        </b-col>
                        <b-col>
                            <div class="h-100 d-flex">
                                <b-list-group-item href="#" class="w-50 justify-content-center align-self-center"
                                                   @click="$emit('show-single-profile', row.item)"
                                                   variant="primary">
                                    <h3>Show Full Profile</h3>
                                </b-list-group-item>
                            </div>
                        </b-col>
                    </b-row>

                </b-card>
            </template>

        </b-table>
        <div class="text-center my-2" v-if="profileList.length === 0 && !loading">
            <strong>Can't find any profiles!</strong>
        </div>
        <b-row>
            <b-col cols="2">
                <b-form-group
                        id="numItems-field"
                        label-for="perPage">
                    <b-form-select :options="optionViews"
                                   id="perPage"
                                   size="sm"
                                   trim v-model="perPage">
                    </b-form-select>
                </b-form-group>
            </b-col>
            <b-col>
                <b-pagination
                        ref="pagination"
                        :per-page="perPage"
                        :total-rows="rows"
                        align="center"
                        aria-controls="profiles"
                        first-text="First"
                        last-text="Last"
                        size="sm"
                        v-model="currentPage">
                </b-pagination>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import ViewProfile from "../dash/viewProfile";
    import BadgeList from "../badges/badgeTable";
    export default {
        name: "profileList",

        components: {
            BadgeList,
            ViewProfile
        },

        props: {
            profileList: Array, // List of profiles to display
            adminView: Boolean,
            profile: Object, // Profile viewing the list
            loading: {
                default: function() {
                    return false;
                }
            },
            searchingProfiles: {
                default: function () {
                    return false;
                }
            },
            moreResults: {
                default: function() {
                    return false;
                }
            },
            searchParameters: Object,
            firstPage: Boolean,
            refreshTable: Boolean
        },

        watch: {
            loading() {
                // Retrieve the total number of rows to display in the table for the pagination.
                this.getRows();
            },

            perPage() {
                // Request more profiles.
                this.$emit('get-more', this.currentPage-1, this.perPage);
            },

            currentPage() {
                // Request more profiles.
                this.$emit('get-more', this.currentPage-1, this.perPage);
            },

            firstPage() {
                // Reset the pagination to the first page.
                this.currentPage = 1;
            },

            refreshTable() {
                this.$refs['profilesTable'].refresh()
            }
        },

        data() {
            return {
                optionViews: [
                {value: 1, text: "1"},
                {value: 5, text: "5"},
                {value: 10, text: "10"},
                {value: 15, text: "15"},
                {value: Infinity, text: "All"}],
                perPage: 5,
                currentPage: 1,
                rows: null
            }
        },

        computed: {
            fields() {
                if (this.adminView) {
                    return [
                        {key: 'achievementTracker.rank', label: "Rank", sortable: true, class: 'tableWidthSmall'},
                        {key: 'achievementTracker.points', label: "Points", sortable: true, class: 'tableWidthSmall'},
                        {key: 'profilePhoto', label: "Photo", sortable: false, class: 'tableWidthSmall'},
                        {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'actions', class: 'tableWidthMedium'}
                    ]
                }
                return [
                    {key: 'achievementTracker.rank', label: "Rank", sortable: true, class: 'tableWidthSmall'},
                    {key: 'achievementTracker.points', label: "Points", sortable: true, class: 'tableWidthSmall'},
                    {key: 'profilePhoto', label: "Photo", sortable: false, class: 'tableWidthSmall'},
                    {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'nationalities', label: "Nationalities", sortable: false, class: 'tableWidthSmall'},
                    {key: 'travellerTypes', label: "Traveller Types", sortable: false, class: 'tableWidthSmall'},
                    {key: 'actions', class: 'tableWidthMedium'}
                ]

            }
        },

        methods: {
            /**
             * Used to calculate a specific rows nationalities from their list of nationalities. Shows all the
             * nationalities in the row.
             *
             * @param nationalities     the row's (profile) nationalities.
             */
            calculateNationalities(nationalities) {
                let nationalityList = "";
                for (let i = 0; i < nationalities.length; i++) {
                    if (nationalities[i + 1] !== undefined) {
                        nationalityList += nationalities[i].nationality + ", \n";
                    } else {
                        nationalityList += nationalities[i].nationality;
                    }

                }
                return nationalityList;
            },


            /**
             * Used to calculate a specific rows traveller types from their list of traveller types. Shows all the
             * traveller types in the row.
             *
             * @param travellerTypes     the row's (profile) traveller types.
             */
            calculateTravellerTypes(travellerTypes) {
                let travTypeList = "";
                for (let i = 0; i < travellerTypes.length; i++) {
                    if (travellerTypes[i + 1] !== undefined) {
                        travTypeList += travellerTypes[i].travellerType + ", \n";
                    } else {
                        travTypeList += travellerTypes[i].travellerType;
                    }

                }
                return travTypeList;
            },


            /**
             * Retrieves the user's primary photo thumbnail, if none is found set to the default image.
             *
             * @param photo         returns a url of which photo should be displayed as the profile picture for the user.
             */
            getProfilePictureThumbnail(photo) {
                if (photo !== null) {
                    let photoId = photo.id;
                    return `/v1/photos/thumb/` + photoId;
                } else {
                    return "../../../static/default_profile_picture.png";
                }
            },


            /**
             * Emits an event on the table being sorted to the parent component containing the column to sort by and
             * order.
             *
             * @param columnSortBy  the column containing the value to sort by and the order.
             */
            sortingChanged(columnSortBy) {
                if (columnSortBy.sortBy === "achievementTracker.rank") {
                    columnSortBy.sortBy = "achievementTracker.points";
                }
                this.$emit('sort-table', {"sortBy": columnSortBy.sortBy, "order": columnSortBy.sortDesc});
            },


            /**
             * Computed function used for the pagination of the table.
             *
             * @returns {number}    the number of rows required in the table based on number of profiles to be
             *                      displayed.
             */
            getRows() {
                let searchQuery = "";
                let self = this;
                if (!this.searchParameters) {
                    searchQuery =
                        "?name=" + "" +
                        "&nationalities=" + "" +
                        "&gender=" + "" +
                        "&min_age=" + "" +
                        "&max_age=" + "" +
                        "&travellerTypes=" + "" +
                        "&rank=" + "";
                    this.searchingProfiles = false;
                } else {
                    searchQuery =
                        "?name=" + this.searchParameters.name +
                        "&nationalities=" + this.searchParameters.nationality +
                        "&gender=" + this.searchParameters.gender +
                        "&min_age=" + this.searchParameters.age[0] +
                        "&max_age=" + this.searchParameters.age[1] +
                        "&travellerTypes=" + this.searchParameters.travellerType +
                        "&rank=" + this.searchParameters.rank;
                    this.searchingProfiles = true;
                }
                if (this.perPage === Infinity) {
                    this.rows = 10;
                } else {
                    fetch(`/v1/profiles/count` + searchQuery, {
                        method: "GET",
                        accept: "application/json"
                    }).then(function (response) {
                        response.json().then(responseBody => {
                            if (response.ok) {
                                self.rows = responseBody;
                            } else {
                                self.showErrorToast(responseBody);
                            }
                        });
                    });
                }
            }
        }
    }
</script>