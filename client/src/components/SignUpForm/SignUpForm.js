import React from "react";
import {Formik, Form, Field} from "formik";
import {
  FormikWrapper,
  SignUpLogo,
  SubmitButton,
  LogInOptions,
  LogInLink,
  RequiredMessage,
  RememberMeText,
  RememberMeWrapper
} from "./SignUpForm-styles.js";
import FormInput from "../FormInput/FormInput.js";
import {signUpSchema, logInSchema} from "./Yup.js";
import {useState, useRef} from "react";
import {authenticate, receiveData} from "../../services/UserService.js";
import {getCookie, setCookie} from "../../services/CookiesService.js";
import {userOperations} from "../../store/user";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import Loader from "../Loader/Loader.js";
import {TOKEN} from "../../util/constants";

export const SignUpForm = () => {
  const [showLogIn, setShowLogIn] = useState(true);
  const [showError, setShowError] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const rememberMeRef = useRef(null);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const swapShowLogIn = () => {
    setShowError(false);
    setShowLogIn(!showLogIn);
  };

  const handleSubmit = async (values) => {
    const userData = {
      username: values.username,
      password: values.password,
      rememberMe: rememberMeRef.current.checked
    };

    try {
      const response = await authenticate(userData, showLogIn ? "registration" : "login")

      setShowError(false);
      dispatch(
          userOperations.setNewUser({
            user: values.username,
            id: response.data.id,
            profileImg: response.data.profileImg,
            following: [],
            followers: [],
          })
      );

      localStorage.setItem(TOKEN, response.data.jwt);
      navigate("/");

    } catch (e) {
        console.log(e.response.data)
      setShowError(true);
      setErrorMessage(e.response.data.message);
    }
  };

  if (getCookie("id")) return <Loader/>;

  return (
      <FormikWrapper>
        <Formik
            validateOnChange={false}
            validateOnBlur={false}
            initialValues={{username: "", password: "", passwordConfirmation: ""}}
            onSubmit={(values) => {
              handleSubmit(values);
            }}
            validationSchema={showLogIn ? signUpSchema : logInSchema}
        >
          {(formikProps) => {
            return (
                <Form noValidate>
                  <SignUpLogo to="/">SimplePlace</SignUpLogo>
                  {showError && <RequiredMessage>{errorMessage}</RequiredMessage>}
                  <div>
                    <Field
                        bdColor={formikProps.errors.username ? "red" : "black"}
                        component={FormInput}
                        name="username"
                        type="text"
                        placeholder="Username"
                    />
                  </div>
                  <div>
                    <Field
                        bdColor={formikProps.errors.password ? "red" : "black"}
                        component={FormInput}
                        name="password"
                        type="password"
                        placeholder="Password"
                    />
                  </div>
                  {showLogIn && (
                      <div>
                        <Field
                            bdColor={
                              formikProps.errors.passwordConfirmation ? "red" : "black"
                            }
                            component={FormInput}
                            name="passwordConfirmation"
                            type="password"
                            placeholder="Confirm password"
                        />
                      </div>
                  )}
                  <RememberMeWrapper>
                    <input type="checkbox" ref={rememberMeRef} style={{cursor: "pointer"}}/>
                    <RememberMeText>Remember me</RememberMeText>
                  </RememberMeWrapper>
                  <div>
                    <SubmitButton>
                      {showLogIn ? "Create account" : "Log in"}
                    </SubmitButton>
                  </div>
                  <LogInOptions>
                    {showLogIn ? "Already have an account?" : "No account?"}
                    <LogInLink onClick={swapShowLogIn}>
                      {showLogIn ? "Log in" : "Sign up"}
                    </LogInLink>
                  </LogInOptions>
                </Form>
            );
          }}
        </Formik>
      </FormikWrapper>
  );
};

export default SignUpForm;
