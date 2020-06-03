package com.idgindigo.pms.web.controller;

/**
 * @author valentyn_vakatsiienko
 * @since 11/14/13 4:41 PM
 */
public class SignupControllerTest extends MultitenantPostgresDbWebTest {
    /*@Inject
    private RoleProvider roleProvider;
    @Inject
    private AuthenticationProvider authenticationProvider;
    @Inject
    private HotelProvider hotelProvider;

    @Test(enabled = false)
    public void testUserSignup() throws Exception {
        Authentication adminAuthentication = authenticationProvider.getTransientEntity();
        User admin = new User();
        admin.setRole(roleProvider.getTransientEntity());
        Hotel hotel = hotelProvider.getTransientEntity();
        SecurityUtils.authenticateUser(adminAuthentication, admin);

        HotelUser userAuthentication = authenticationProvider.hotelUser.getTransientEntity();
        HotelUserDto user = new HotelUserDto();
        user.setAuthentication(userAuthentication);
        user.setPhone("phone");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        SignupController.UserSignupDto dto = new SignupController.UserSignupDto(user, hotel);

        securedMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .session(session)).andExpect(status().isCreated());
    }*/

    @Override
    protected String getUrl() {
        return null;
    }
}
