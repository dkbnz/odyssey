<template>
    <div>
        <h3>Public Photos</h3>
        <photo-table
                :photos="publicPhotos"
                :profile="profile"
                :userProfile="userProfile"
                :showDropdown="false">
        </photo-table>

        <h3>Your Photos</h3>

        <photo-table :selectedImages="personalPhotos"
                     :photos="profile.photoGallery"
                     :profile="profile"
                     :userProfile="userProfile"
                     :showDropdown="true"
                     @privacy-update="updatePhotoPrivacy"
                     @photo-click="photoToggled">
        </photo-table>

        <b-button variant="success" @click="showModal('addRemovePhotosModal')" block>Add/Remove Destination Photo</b-button>
        <b-modal ref="addRemovePhotosModal" id="addRemovePhotosModal" hide-footer centered size="xl">
            <template slot="modal-title"><h3>Personal Photo Gallery</h3></template>
            <photo-table :selectedImages="personalPhotos"
                         :photos="profile.photoGallery"
                         :profile="profile"
                         :userProfile="userProfile"
                         :showDropdown="true"
                         @privacy-update="updatePhotoPrivacy"
                         @photo-click="photoToggled">
            </photo-table>
            <b-button @click="dismissModal('addRemovePhotosModal')" variant="success" block>OK</b-button>
        </b-modal>

    </div>
</template>

<script>
    import PhotoTable from "../photos/photoTable";

    export default {
        name: "destinationGallery",
        data: function () {
            return {
                publicPhotos: [],
                personalPhotos: []
            }
        },

        props: {
            destination: Object,
            profile: Object,
            userProfile: Object
        },
        watch: {
            destination () {
                this.calculatePhotoSplit();
            }
        },

        mounted() {
            this.calculatePhotoSplit();
        },

        methods: {

            /**
             * Calculates which list the destination photos must be put into depending on their privacy. If the photo
             * is public, then it is in the publicPhotos list. Otherwise it is in the personalPhotos list.
             */
            calculatePhotoSplit() {
                this.publicPhotos = [];
                this.personalPhotos = [];
                for (let i = 0; i < this.destination.photoGallery.length; i++) {
                    if (this.destination.photoGallery[i].public.toString() === "true") {
                        this.publicPhotos.push(this.destination.photoGallery[i]);
                    }
                    if (this.indexOfById(this.profile.photoGallery, this.destination.photoGallery[i]) !== -1) {
                        this.personalPhotos.push(this.destination.photoGallery[i]);
                    }
                }
            },

            /**
             * Updates the photos list sent to the photoTable for a single privacy photo.
             *
             * @param photo           the photo that's changing status.
             */
            updatePhotoPrivacy: function(photo) {
                let self = this;

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status === 200) {
                        response.clone().json().then(text => {
                            self.profile.photoGallery = text;
                        });
                    }
                });

                let index = this.indexOfById(this.destination.photoGallery, photo);
                if (index !== -1) {
                    this.destination.photoGallery[index] = photo
                }

                this.calculatePhotoSplit();
            },


            /**
             * Returns the index of an object in the specified array. Comparison is done by the Id
             * attribute. Returns -1 if not found
             *
             * @param arrayToCheck  the array/list to check for the object.
             * @param object        the object to be checked in the array.
             * @returns {int}       index of object in array or -1 if not found
             */
            indexOfById(arrayToCheck, object) {
                for (let i = 0; i < arrayToCheck.length; i++) {
                    if(arrayToCheck[i].id === object.id) {
                        return i;
                    }
                }
                return -1;
            },


            /**
             * Event handler for the photo being added/removed from the destination.
             * If photo is in destination photos, send request to backend and remove from destination photo gallery.
             * Otherwise, send request to add it and add it to destination photo gallery.
             *
             * @param photo     the photo to be added or removed.
             */
            photoToggled(photo) {
                let index = this.indexOfById(this.destination.photoGallery, photo)

                if (index !== -1) {
                    this.removeDestinationPhoto(photo);
                    this.destination.photoGallery.splice(index, 1)
                } else {
                    this.addDestinationPhoto(photo);
                    this.destination.photoGallery.push(photo)
                }

                this.calculatePhotoSplit()
            },


            /**
             * Send a POST request to the backend to add a specified photo to the destination being viewed.
             *
             * @param photo the photo to be added to the destination.
             */
            addDestinationPhoto(photo) {
                let self = this;
                fetch('/v1/destinationPhotos/' + this.destination.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status !== 201) {
                        self.showError = true;
                        self.alertMessage = "An error occurred when adding a destination photo";
                    }
                });
            },


            /**
             * Send a DELETE request to the backend to remove a specified photo to the destination being viewed.
             *
             * @param photo     photo object that needs to be removed from a destination.
             */
            removeDestinationPhoto(photo) {
                let self = this;
                fetch('/v1/destinationPhotos/' + this.destination.id, {
                    method: 'DELETE',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status !== 200) {
                        self.showError = true;
                        self.alertMessage = "An error occurred when deleting a destination photo";
                    }
                });
            },

            /**
             * Used to dismiss the Add a Photo to the Destination modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },
            },

            showModal(modal) {
                this.$refs[modal].show();
            },

        },

        components: {
            PhotoTable
        }
    }
</script>