package com.businessapi.util;

import com.businessapi.exception.ErrorType;
import com.businessapi.exception.StockServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionManager
{
    public static Long memberId;
    public static Long getMemberIdFromAuthenticatedMember()
    {
        // Gets authId from authenticated member
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        memberId = Long.parseLong(authentication.getName());
        return Long.parseLong(authentication.getName());
    }

    public static void authorizationCheck(Long entityMemberId)
    {
        if (!entityMemberId.equals(memberId))
        {
            throw new StockServiceException(ErrorType.UNAUTHORIZED);
        }
    }
}
