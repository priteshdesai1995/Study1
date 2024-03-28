<section class="vh-100" style="background-color: #9A616D;">
	<div class="container py-5 h-100">
		<div
			class="row d-flex justify-content-center align-items-center h-100">
			<div class="col col-xl-10">
				<div class="card" style="border-radius: 1rem;">
					<div class="row g-0">
						<div class="col-md-6 col-lg-5 d-none d-md-block"></div>
						<div class="col-md-6 col-lg-7 d-flex align-items-center">
							<div class="card-body p-4 p-lg-5 text-black">
								${SPRING_SECURITY_LAST_EXCEPTION.message}

								<h5 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;">Sign
									into your account Override</h5>

								<form method="POST" action="login">
									<div class="form-outline mb-4">
										<input type="text" id="username" name="username"
											class="form-control form-control-lg" /> <label
											class="form-label" name="username" for="form2Example17">Email
											address</label>
									</div>

									<div class="form-outline mb-4">
										<input type="password" id="password" name="password"
											class="form-control form-control-lg" /> <label
											class="form-label" for="form2Example27">Password</label>
									</div>

									<div class="pt-1 mb-4">
										<input class="btn btn-dark btn-lg btn-block" type="submit"
											name="Login" value="Submit" />
									</div>
								</form>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>