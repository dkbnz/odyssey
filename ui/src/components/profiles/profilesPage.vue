<template>
    <div v-if="profile.length !== 0">
        <nav-bar-main v-if="!adminView" v-bind:profile="profile"></nav-bar-main>

        <div class="container">

            <h1 class="page_title" v-if="!adminView">Find Profiles</h1>
            <p class="page_title" v-if="!adminView"><i>Search for other travellers using any of the fields in the form below</i></p>

            <b-alert v-model="showError" variant="danger" dismissible>There's something wrong in the form!</b-alert>


            <!-- Confirmation modal for deleting a profile. -->
            <b-modal ref="deleteModal" id="deleteModal" hide-footer title="Delete Profile">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedProfile.firstName}} {{selectedProfile.lastName}}"?
                </div>
                <b-button
                        class="mr-2 float-right"
                        variant="danger"
                        @click="dismissModal()
                         deleteUser(selectedProfile);">Delete
                </b-button>
                <b-button
                        class="mr-2 float-right"
                        @click="dismissModal()">Cancel
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
                            <b-form-select id="nationality" v-model="searchNationality" trim>
                                <template slot="first">
                                    <option :value="null">-- Any --</option>
                                </template>
                                <option v-for="nationality in nationalityOptions"
                                        :value="nationality.nationality">
                                    {{nationality.nationality}}</option>
                            </b-form-select>
                        </b-form-group>
                    </b-col>
                    <b-col>
                        <b-form-group
                                id="gender-field"
                                label="Gender:"
                                label-for="gender">
                            <b-form-select id="gender" v-model="searchGender" :options="genderOptions" trim>
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
                        <b-form-input id="minAge" v-model="searchMinAge"
                                      :type="'range'"
                                      min="0"
                                      max="110"
                                      trim></b-form-input>
                        </b-form-group>
                    </b-col>
                    <b-col>
                        <b-form-group
                        id="maxAge-field"
                        label="Max Age:"
                        label-for="maxAge">
                        <div class="mt-2">{{ searchMaxAge }}</div>
                        <!--Range slider from 0 to 110-->
                        <b-form-input id="maxAge" v-model="searchMaxAge"
                                      :type="'range'"
                                      min="0"
                                      max="110"
                                      trim></b-form-input>
                        </b-form-group>
                    </b-col>
                </b-row>

                <b-form-group
                        id="travType-field"
                        label="Traveller Type:"
                        label-for="travType">
                    <b-form-select id="travType" v-model="searchTravType" trim>
                        <template>
                            <option :value="null" selected="selected">-- Any --</option>
                        </template>
                        <option v-for="travType in travTypeOptions"
                                :value="travType.travellerType">
                            {{travType.travellerType}}</option>
                    </b-form-select>
                </b-form-group>

                <b-button block variant="primary" @click="searchProfiles">Search</b-button>
            </div>

            <!--Displays results from profile search in a table format-->
            <div style="margin-top: 40px">
                <b-table hover striped outlined fixed
                         id="profiles"
                         ref="profilesTable"
                         :items="profiles"
                         :fields="fields"
                         :per-page="perPage"
                         :current-page="currentPage"
                         :sort-by.sync="sortBy"
                         :sort-desc.sync="sortDesc"
                         :busy="this.profiles.length === 0">
                    <div slot="table-busy" class="text-center my-2">
                        <b-spinner v-if="retrievingProfiles" class="align-middle"></b-spinner>
                        <strong>Can't find any profiles!</strong>
                    </div>
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
                                <b-button v-if="profile.isAdmin && !(row.item.isAdmin) && row.item.id !== 1" size="sm"
                                          @click="makeAdmin(row.item)" variant="success" class="mr-2" block>
                                    Make Admin
                                </b-button>
                                <b-button v-if="profile.isAdmin && row.item.isAdmin && row.item.id !== 1"
                                          :disabled="row.item.id===1" variant="danger" size="sm"
                                          @click="removeAdmin(row.item)" class="mr-2" block>
                                    Remove Admin
                                </b-button>
                                <!--<b-button size="sm" @click="emitAdminEdit(row.item)" variant="warning" class="mr-2" block>-->
                                    <!--Show More Details-->
                                <!--</b-button>-->
                                <b-button size="sm" block @click="emitAdminEdit(row.item)" variant="warning" class="mr-2">
                                    Show More Details
                                </b-button>

                                <b-button v-if="profile.isAdmin && row.item.id !== 1" :disabled="row.item.id===1"
                                          size="sm" variant="danger" v-b-modal.deleteModal
                                          @click="sendProfileToModal(row.item)" class="mr-2" block>
                                    Delete
                                </b-button>

                        </b-row>
                        <!-- If user is not admin, can only see other profiles -->
                        <b-row class="text-center" v-else>
                                <b-button size="sm" block @click="row.toggleDetails" variant="warning" class="mr-2">
                                    {{ row.detailsShowing ? 'Hide' : 'Show'}} More Details
                                </b-button>
                        </b-row>
                    </template>
                    <template slot="row-details" slot-scope="row">
                        <b-card>
                            <view-profile :profile="row.item"
                                          :userProfile="profile"
                                          :admin-view="adminView"
                                          :destinations="destinations">
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
                            <b-form-select id="perPage" v-model="perPage"
                                           :options="optionViews"
                                           size="sm"
                                           trim> </b-form-select>
                        </b-form-group>
                    </b-col>
                    <b-col cols="8">
                        <b-pagination
                                v-model="currentPage"
                                :total-rows="rows"
                                :per-page="perPage"
                                aria-controls="my-table"
                                first-text="First"
                                last-text="Last"
                                align="center"
                                size="sm"
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
                default: function() {
                    return 10;
                }
            }
        },
        components: {
            ViewProfile,
            NavBarMain,
            FooterMain,
            Dash,
            UnauthorisedPrompt
        },
        data: function() {
            return {
                sortBy: 'firstName',
                sortDesc: false,
                showError: false,
                searchNationality: "",
                searchGender: "",
                searchMinAge: 0,
                searchMaxAge: 100,
                searchTravType: "",
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                currentPage: 1,
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ],
                fields: [{key:'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                    {key:'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                    {key:'nationalities', label: "Nationalities", sortable: true, class: 'tableWidthMedium'},
                    {key:'gender', value: 'gender', sortable: true, class: 'tableWidthSmall'},
                    {key:'age', value:'age', sortable: true, class: 'tableWidthSmall'},
                    {key:'travellerType', label: "Traveller Types" , sortable: true, class: 'tableWidthMedium'},
                    {key:'actions', class: 'tableWidthMedium'}],
                profiles: [],
                retrievingProfiles: false,
                selectedProfile: ""
            }
        },
        mounted() {
            this.queryProfiles();
        },
        methods: {
            /**
             * Used to calculate a specific rows nationalities from their list of nationalities. Shows all the
             * nationalities in the row.
             * @param nationalities     the row's (profile) nationalities.
             */
            calculateNationalities (nationalities) {
                let nationalityList = "";
                for (let i = 0; i < nationalities.length; i++) {
                    if (nationalities[i+1] !== undefined) {
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
             * @param travellerTypes     the row's (profile) traveller types.
             */
            calculateTravTypes (travellerTypes) {
                let travTypeList = "";
                for (let i = 0; i < travellerTypes.length; i++) {
                    if (travellerTypes[i+1] !== undefined) {
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
             * @param makeAdminProfile      the selected profile to be made an admin.
             */
            makeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/makeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function() {
                    self.searchProfiles();
                })
            },

            /**
             * Method to remove admin attribute from a user. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             * @param makeAdminProfile      the selected profile to be removed as an admin.
             */
            removeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/removeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function() {
                    self.searchProfiles();
                })
            },

            /**
             * Method to delete a user's profile. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             * @param deleteUser      the selected profile to be deleted.
             */
            deleteUser(deleteUser) {
                let self = this;
                fetch('/v1/profile/' + deleteUser.id, {
                    method: 'DELETE',
                }).then(function() {
                    self.searchProfiles();
                })
            },

            /**
             * Changes fields so that they can be used in searching
             * Runs validation on range fields
             */
            searchProfiles() {
                this.searchMinAge = parseInt(this.searchMinAge);
                this.searchMaxAge = parseInt(this.searchMaxAge);
                if (isNaN(this.searchMinAge) || isNaN(this.searchMaxAge)) {
                    this.showError = true;
                } else if (this.searchMinAge > this.searchMaxAge) {
                    this.showError = true;
                }
                else {
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
             * Queries database for profiles which fit search criteria
             */
            queryProfiles() {
                this.retrievingProfiles = true;
                let searchQuery =
                    "?nationality=" + this.searchNationality +
                    "&gender=" + this.searchGender +
                    "&min_age=" + this.searchMinAge +
                    "&max_age=" + this.searchMaxAge +
                    "&traveller_type=" + this.searchTravType;
                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.retrievingProfiles = false;
                        this.profiles = data;
                    })
            },

            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             * @param response, passed back to the getAllTrips function to be parsed into a json.
             * @returns throws the error.
             */
            checkStatus (response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error); // eslint-disable-line no-console
                throw error;
            },

            /**
             * Used to turn the response of the fetch method into a usable JSON.
             * @param response of the fetch method.
             * @returns the json body of the response.
             */
            parseJSON (response) {
                return response.json();
            },

            /**
             * Used to send a selected profile to a modal so the admin can confirm they want to delete the selected
             * profile.
             * @param profile, the profile that is selected to be deleted.
             */
            sendProfileToModal(profile) {
                this.selectedProfile = profile;
            },

            /**
             * Used to dismiss the delete a profile modal
             */
            dismissModal() {
                this.$refs['deleteModal'].hide();
            },

            /**
             * Emits the event that the admin is viewing a profile to edit, this will navigate to the edit page.
             */
            emitAdminEdit(profile) {
                this.$emit('admin-edit', profile);
            }

        },
        computed: {
            rows() {
                /**
                 * @returns the number of rows required in the table based on number of profiles to be displayed
                 */
                return this.profiles.length
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