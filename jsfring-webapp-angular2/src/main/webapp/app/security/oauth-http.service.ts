import {Observable} from "rxjs/Observable";

import {Injectable} from "@angular/core";
import {Headers, Http, RequestOptions, RequestOptionsArgs, Response} from "@angular/http";

import {SecurityService} from "./security.service";
import {UnauthorizedObservable} from "./unauthorized-observable";

/** Proxy of {@link Http}, who add OAuth 2 {@code "Authorization"} header to each requests. */
@Injectable()
export class OAuthHttp {

    constructor(
        protected http: Http,
        private securityService: SecurityService,
        private unauthorizedObservable: UnauthorizedObservable
    ) {}

    /**
     * Add "Authorization" key to header, if not present.
     * @param options The {@link RequestOptionsArgs} to initialize.
     * @return The initialized {@link RequestOptionsArgs}.
     *         The new {@link RequestOptionsArgs} if not present.
     */
    private addAuthorizationHeader(options: RequestOptionsArgs): RequestOptionsArgs {
        /*if (options == null)
            options = new RequestOptions();
        if (options.headers == null)
            options.headers = new Headers();
        if (!options.headers.has('Authorization'))
            options.headers.append('Authorization', 'Bearer ' + this.securityService.token);*/
        return options;
    }

    // Proxyfied methods

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        let response: Observable<Response> = this.http.get(url, options);
        response.subscribe(
            r => {},
            e => this.unauthorizedObservable.observable.emit(e));
        return response;
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.http.post(url, body, options);
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.http.put(url, body, options);
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.http.delete(url, options);
    }

    patch(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.http.patch(url, body, options);
    }

    head(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.http.head(url, options);
    }

}

import {ConnectionBackend} from "@angular/http";

/** Extension of {@link Http}, who add OAuth 2 {@code "Authorization"} header to each requests. */
@Injectable()
export class OAuthHttp_old2 extends Http {

    constructor(
        //_backend: ConnectionBackend,
        _defaultOptions: RequestOptions,
        private securityService: SecurityService
    ) {
        //super(_backend, _defaultOptions);
        super(null, null);
        console.log('OAuthHttp::constructor');
    }

    /**
     * Add "Authorization" key to header, if not present.
     * @param options The {@link RequestOptionsArgs} to initialize.
     * @return The initialized {@link RequestOptionsArgs}.
     *         The new {@link RequestOptionsArgs} if not present.
     */
    private addAuthorizationHeader(options: RequestOptionsArgs): RequestOptionsArgs {
        if (options == null)
            options = new RequestOptions();
        if (options.headers == null)
            options.headers = new Headers();
        if (!options.headers.has('Authorization'))
            options.headers.append('Authorization', 'Bearer ' + this.securityService.token);
        return options;
    }

    // Proxyfied methods

    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        let response: Observable<Response> = this.get(url, options);
        /*response.subscribe(
         r => {},
         e => this.unauthorizedObservable.observable.emit(e));*/
        return response;
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.post(url, body, options);
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.put(url, body, options);
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.delete(url, options);
    }

    patch(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.patch(url, body, options);
    }

    head(url: string, options?: RequestOptionsArgs): Observable<Response> {
        options = this.addAuthorizationHeader(options);
        return this.head(url, options);
    }

}

@Injectable()
export class OAuthRequestOptions extends RequestOptions {

    public constructor(securityService: SecurityService) {
        super({});
        securityService.onConnect.subscribe(() => {
            this.headers.append('Authorization', 'Bearer ' + securityService.token);
            console.log("User's OAuth token initialized");
        });
    }

}