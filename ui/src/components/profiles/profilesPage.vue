<template>
    <div v-if="profile.length !== 0">
        <nav-bar-main v-bind:profile="profile" v-if="!adminView"></nav-bar-main>

        <div :class="containerClass">

            <h1 class="page-title" v-if="!adminView">Find Profiles</h1>
            <p class="page-title" v-if="!adminView"><i>Search for other travellers using any of the fields in the form
                below</i>
            </p>

            <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>


            <!-- Confirmation modal for deleting a profile. -->
            <b-modal ref="deleteProfileModal" id="deleteProfileModal" hide-footer title="Delete Profile">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedProfile.firstName}} {{selectedProfile.lastName}}"?
                </div>
                <b-button
                        @click="dismissModal()
                         deleteUser(selectedProfile);"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissModal()"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>


            <div>
                <!--Input fields for searching profiles-->
                <b-row>
                    <b-col>
                        <b-form-group
                                id="nationalities-field"
                                label="Nationality:"
                                label-for="nationality">
                            <b-form-select id="nationality" trim v-model="searchNationality">
                                <template slot="first">
                                    <option :value="null">-- Any --</option>
                                </template>
                                <option :value="nationality.nationality"
                                        v-for="nationality in nationalityOptions">
                                    {{nationality.nationality}}
                                </option>
                            </b-form-select>
                        </b-form-group>
                    </b-col>
                    <b-col>
                        <b-form-group
                                id="gender-field"
                                label="Gender:"
                                label-for="gender">
                            <b-form-select :options="genderOptions" id="gender" trim v-model="searchGender">
                                <template slot="first">
                                    <option :value="null">-- Any --</option>
                                </template>
                            </b-form-select>
                        </b-form-group>
                    </b-col>
                </b-row>
                <b-row>
                    <b-col>
                        <b-form-group
                                id="minAge-field"
                                label="Min Age: "
                                label-for="minAge">
                            <div class="mt-2">{{ searchMinAge }}</div>

                            <!--Range slider from 0 to 110-->
                            <b-form-input :type="'range'" id="minAge"
                                          max="110"
                                          min="0"
                                          trim
                                          v-model="searchMinAge"></b-form-input>
                        </b-form-group>
                    </b-col>
                    <b-col>
                        <b-form-group
                                id="maxAge-field"
                                label="Max Age:"
                                label-for="maxAge">
                            <div class="mt-2">{{ searchMaxAge }}</div>
                            <!--Range slider from 0 to 110-->
                            <b-form-input :type="'range'" id="maxAge"
                                          max="120"
                                          min="0"
                                          trim
                                          v-model="searchMaxAge"></b-form-input>
                        </b-form-group>
                    </b-col>
                </b-row>

                <b-form-group
                        id="travType-field"
                        label="Traveller Type:"
                        label-for="travType">
                    <b-form-select id="travType" trim v-model="searchTravType">
                        <template>
                            <option :value="null" selected="selected">-- Any --</option>
                        </template>
                        <option :value="travType.travellerType"
                                v-for="travType in travTypeOptions">
                            {{travType.travellerType}}
                        </option>
                    </b-form-select>
                </b-form-group>

                <b-button @click="searchProfiles" block variant="primary">Search</b-button>
            </div>

            <!--Displays results from profile search in a table format-->
            <div style="margin-top: 40px">
                <b-table :busy="this.profiles.length === 0"
                         :current-page="currentPage"
                         :fields="fields"
                         :items="profiles"
                         :per-page="perPage"
                         :sort-by.sync="sortBy"
                         :sort-desc.sync="sortDesc"
                         responsive
                         hover
                         id="profiles"
                         outlined
                         ref="profilesTable"
                         striped>
                    <div class="text-center my-2" slot="table-busy">
                        <b-spinner class="align-middle" v-if="retrievingProfiles"></b-spinner>
                        <strong>Can't find any profiles!</strong>
                    </div>
                    <template slot="profilePhoto" slot-scope="row">
                        <b-img :src="getProfilePictureThumbnail(row.item.profilePicture)"
                               fluid
                               rounded="circle"
                               thumbnail
                               @error="imageAlt">
                        </b-img>
                    </template>
                    <template slot="nationalities" slot-scope="row">
                        {{calculateNationalities(row.item.nationalities)}}
                    </template>

                    <template slot="travellerType" slot-scope="row">
                        {{calculateTravTypes(row.item.travellerTypes)}}
                    </template>

                    <!--Shows more details about any profile-->
                    <template slot="actions" slot-scope="row">
                        <!-- If user is admin, can delete, make/remove admin rights and delete other users -->
                        <b-row class="text-center" v-if="profile.isAdmin && adminView">
                            <b-button @click="makeAdmin(row.item)" block
                                      class="mr-2" size="sm"
                                      v-if="profile.isAdmin && !(row.item.isAdmin) && row.item.id !== 1"
                                      variant="success">
                                Make Admin
                            </b-button>
                            <b-button :disabled="row.item.id===1"
                                      @click="removeAdmin(row.item)" block class="mr-2"
                                      size="sm" v-if="profile.isAdmin && row.item.isAdmin && row.item.id !== 1"
                                      variant="danger">
                                Remove Admin
                            </b-button>
                            <b-button @click="emitAdminEdit(row.item)" block class="mr-2" size="sm" variant="warning">
                                Show More Details
                            </b-button>
                            <b-button :disabled="row.item.id===1" @click="sendProfileToModal(row.item)"
                                      block class="mr-2" size="sm"
                                      v-b-modal.deleteProfileModal v-if="profile.isAdmin && row.item.id !== 1"
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
                                    :admin-view="adminView"
                                    :destinations="destinations"
                                    :profile="row.item"
                                    :userProfile="profile">
                            </view-profile>
                        </b-card>
                    </template>

                </b-table>

                <!--Pagination and results per page settings-->
                <b-row>
                    <b-col cols="1">
                        <b-form-group
                                id="profiles-field"
                                label-for="perPage">
                            <b-form-select :options="optionViews" id="perPage"
                                           size="sm"
                                           trim
                                           v-model="perPage"></b-form-select>
                        </b-form-group>
                    </b-col>
                    <b-col cols="8">
                        <b-pagination
                                :per-page="perPage"
                                :total-rows="rows"
                                align="center"
                                aria-controls="my-table"
                                first-text="First"
                                last-text="Last"
                                size="sm"
                                v-model="currentPage"
                        ></b-pagination>
                    </b-col>
                </b-row>

            </div>
        </div>
        <footer-main></footer-main>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import ViewProfile from '../dash/viewProfile.vue'
    import Dash from '../dash/dashPage'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'

    export default {
        name: "profilesPage",

        props: {
            profile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            adminView: Boolean,
            destinations: Array,
            perPage: {
                default: function () {
                    return 10;
                }
            },
            containerClass: {
                default: function() {
                    return 'containerMain';
                }
            },
            destinationTypes: Array
        },

        components: {
            ViewProfile,
            NavBarMain,
            FooterMain,
            Dash,
            UnauthorisedPrompt
        },

        data: function () {
            return {
                sortBy: 'firstName',
                sortDesc: false,
                showError: false,
                searchNationality: "",
                searchGender: "",
                searchMinAge: 0,
                searchMaxAge: 120,
                searchTravType: "",
                optionViews: [{value: 1, text: "1"}, {value: 5, text: "5"}, {value: 10, text: "10"}, {
                    value: 15,
                    text: "15"
                }],
                currentPage: 1,
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ],
                profiles: [],
                retrievingProfiles: false,
                selectedProfile: "",
                alertMessage: ""
            }
        },

        mounted() {
            this.queryProfiles();
        },

        computed: {
            /**
             * @returns the number of rows required in the table based on number of profiles to be displayed.
             */
            rows() {
                return this.profiles.length;
            },


            /**
             * Returns the fields that will be displayed on the table of profiles, depending on if the admin is looking
             * at the page or not.
             */
            fields() {
                if (!this.adminView) {
                    return [
                        {key: 'profilePhoto', label: "Photo", sortable: true, class: 'tableWidthSmall'},
                        {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'nationalities', label: "Nationalities", sortable: true, class: 'tableWidthMedium'},
                        {key: 'gender', value: 'gender', sortable: true, class: 'tableWidthSmall'},
                        {key: 'age', value: 'age', sortable: true, class: 'tableWidthSmall'},
                        {key: 'travellerType', label: "Traveller Types", sortable: true, class: 'tableWidthMedium'},
                        {key: 'actions', class: 'tableWidthMedium'}]
                }
                return [
                    {key: 'profilePhoto', label: "Photo", sortable: true, class: 'tableWidthSmall'},
                    {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'actions', class: 'tableWidthMedium'}]
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
                        nationalityList += nationalities[i].nationality + ", ";
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
            calculateTravTypes(travellerTypes) {
                let travTypeList = "";
                for (let i = 0; i < travellerTypes.length; i++) {
                    if (travellerTypes[i + 1] !== undefined) {
                        travTypeList += travellerTypes[i].travellerType + ", ";
                    } else {
                        travTypeList += travellerTypes[i].travellerType;
                    }

                }
                return travTypeList;
            },


            /**
             * Method to make a user an admin. This method is only available if the currently logged in user is an
             * admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be made an admin.
             */
            makeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/makeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Method to remove admin attribute from a user. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be removed as an admin.
             */
            removeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/removeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                }).then(function() {
                    if (self.profile.id === makeAdminProfile.id) {
                        self.$router.push("/dash");
                        self.$router.go();
                    }
                })
            },


            /**
             * Method to delete a user's profile. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param deleteUser      the selected profile to be deleted.
             */
            deleteUser(deleteUser) {
                let self = this;
                fetch('/v1/profile/' + deleteUser.id, {
                    method: 'DELETE',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Changes fields so that they can be used in searching.
             * Runs validation on range fields.
             */
            searchProfiles() {
                this.searchMinAge = parseInt(this.searchMinAge);
                this.searchMaxAge = parseInt(this.searchMaxAge);
                if (isNaN(this.searchMinAge) || isNaN(this.searchMaxAge)) {
                    this.showError = true;
                    this.alertMessage = "Min or Max Age are not numbers";
                } else if (this.searchMinAge > this.searchMaxAge) {
                    this.showError = true;
                    this.alertMessage = "Min age is greater than max age";
                } else {
                    if (this.searchTravType === null) {
                        this.searchTravType = "";
                    }
                    if (this.searchNationality === null) {
                        this.searchNationality = "";
                    }
                    if (this.searchGender === null) {
                        this.searchGender = "";
                    }
                    this.showError = false;
                    this.queryProfiles();
                }
            },


            /**
             * Queries database for profiles which fit search criteria.
             */
            queryProfiles() {
                this.retrievingProfiles = true;
                let searchQuery =
                    "?nationalities=" + this.searchNationality +
                    "&gender=" + this.searchGender +
                    "&min_age=" + this.searchMinAge +
                    "&max_age=" + this.searchMaxAge +
                    "&travellerTypes=" + this.searchTravType;
                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.retrievingProfiles = false;
                        this.profiles = data;
                    })
            },


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
            },


            /**
             * Displays default image when no image is found.
             * @param event     image error event.
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_profile_picture.png"
            },


            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             *
             * @param response, passed back to the getAllTrips function to be parsed into a Json.
             * @returns throws the error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                this.showError = true;
                response.clone().text().then(text => {
                    this.alertMessage = text;
                });
                console.log(error); // eslint-disable-line no-console
                throw error;
            },


            /**
             * Used to turn the response of the fetch method into a usable Json.
             *
             * @param response of the fetch method.
             * @returns the Json body of the response.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Used to send a selected profile to a modal so the admin can confirm they want to delete the selected
             * profile.
             *
             * @param profile, the profile that is selected to be deleted.
             */
            sendProfileToModal(profile) {
                this.selectedProfile = profile;
            },


            /**
             * Used to dismiss the delete a profile modal.
             */
            dismissModal() {
                this.$refs['deleteProfileModal'].hide();
            },


            /**
             * Emits the event that the admin is viewing a profile to edit, this will navigate to the edit page.
             */
            emitAdminEdit(profile) {
                this.$emit('admin-edit', profile);
            }

        }
    }
</script>

<style>
    .tableWidthSmall {
        max-width: 8%;
        width: 8%;
        text-align: center;
    }

    .tableWidthMedium {
        max-width: 8%;
        width: 15%;
        text-align: center;
    }

    .tableWidthLarge {
        max-width: 8%;
        width: 25%;
        text-align: center;
    }
</style>